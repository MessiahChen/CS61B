package gitlet;

import java.io.Serializable;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.ArrayList;
import java.util.Set;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

/**
 * Gitlet class.
 *
 * @author MingjieChen.
 */
public class Gitlet implements Serializable {

    /**
     * Constructor.
     */
    public Gitlet() {
        _currentBranch = null;
        _head = null;
        _branchCommits = new HashMap<>();
        _allCommits = new LinkedHashMap<>();
        _remove = new HashSet<>();
        _staging = new HashSet<>();
        _rmTmp = new HashSet<>();
    }

    /**
     * Serialize.
     *
     * @param gitlet   gitlet.
     * @param filename filename.
     * @throws IOException
     */
    public static void serialize(Gitlet gitlet, String filename)
            throws IOException {
        if (gitlet != null) {
            File file = new File(filename);
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(gitlet);
            out.close();
            fileOut.close();
        }
    }

    /**
     * Deserialize.
     *
     * @param filename filename.
     * @return Gitlet.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static Gitlet deserialize(String filename)
            throws IOException, ClassNotFoundException {
        Gitlet gitlet = null;
        File file = new File(filename);
        if (file.exists()) {
            FileInputStream fileIn = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            gitlet = (Gitlet) in.readObject();
            in.close();
            fileIn.close();
        }
        return gitlet;
    }

    /**
     * Initialized files:
     * /stage to contain temporary files added but not committed.
     * Set current branch to master.
     * Add commit to the LinkedList of the branch.
     *
     * @throws IOException
     */
    public void init() throws IOException {
        if (!Files.exists(Paths.get(".gitlet"))) {
            Files.createDirectory(Paths.get(".gitlet"));
            Files.createDirectory(Paths.get(".gitlet/staging"));
            Files.createDirectory(Paths.get(".gitlet/blobs"));

            _currentBranch = "master";
            Commit commit = new Commit();
            _head = commit;

            if (_branchCommits.get(_currentBranch) == null) {
                LinkedList<Commit> com = new LinkedList<>();
                com.add(commit);
                _branchCommits.put(_currentBranch, com);
            } else {
                _branchCommits.get(_currentBranch).add(commit);
            }
            _allCommits.put(commit.getId(), commit);


        } else {
            System.out.println("A Gitlet version-control "
                    + "system already exists in the "
                    + "current directory.");
        }
    }

    /**
     * Check condition.
     * Write to staging.
     *
     * @param filename filename.
     */
    public void add(String filename) {
        String[] name = filename.split("/");
        String pureFilename = name[name.length - 1];

        if (!new File(filename).exists()) {
            System.out.println("File does not exist.");
            return;
        }

        if (_remove.contains(pureFilename)) {
            _remove.remove(pureFilename);
        }

        _staging.add(pureFilename);

        File oldFile = new File(filename);
        File newFile = new File(".gitlet/staging/" + pureFilename);
        String s1 = Utils.readContentsAsString(oldFile);
        if (_head.getBlobs().size() != 0) {
            for (String file : _head.getFileWithBlob().keySet()) {
                String b = _head.getFileWithBlob().get(file);
                File f = new File(".gitlet/blobs/" + b);
                String s2 = Utils.readContentsAsString(f);
                if (s1.equals(s2) && file.equals(filename)) {
                    newFile.delete();
                    _staging.remove(pureFilename);
                }
            }
        }

        Utils.writeContents(newFile, Utils.readContents(oldFile));
    }

    /**
     * Create and write staging content to blob.
     * Update commit.getFileWithBlob.
     * Update _branchCommits & _allCommits.
     * Update _head.
     *
     * @param msg msg.
     */
    public void commit(String msg) {
        if (_staging.size() == 0 && _remove.size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }

        Commit commit = new Commit(_head, msg);
        @SuppressWarnings("unchecked")
        HashSet<String> tmpStaging = (HashSet<String>) _staging.clone();
        for (String s : tmpStaging) {
            commit.getTrackedFiles().add(s);
            File from = new File(".gitlet/staging/" + s);
            String code = Utils.readContentsAsString(from);
            code += new Random().toString();
            String blobId = Utils.sha1(code);
            File to = new File(".gitlet/blobs/" + blobId);
            Utils.writeContents(to, Utils.readContents(from));
            commit.getFileWithBlob().put(s, blobId);

            _staging.remove(s);
            from.delete();
        }

        for (String s : _rmTmp) {
            commit.getTrackedFiles().remove(s);
            commit.getFileWithBlob().remove(s);
        }
        _rmTmp.clear();

        for (String s : _remove) {
            commit.getFileWithBlob().remove(s);
            _remove.remove(s);
        }

        commit.setId();
        if (_branchCommits.get(_currentBranch) == null) {
            LinkedList<Commit> com = new LinkedList<>();
            com.add(commit);
            _branchCommits.put(_currentBranch, com);
        } else {
            _branchCommits.get(_currentBranch).add(commit);
        }
        _allCommits.put(commit.getId(), commit);
        _head = commit;
    }

    /**
     * MergeCommit.
     *
     * @param commit commit.
     */
    public void mergeCommit(Commit commit) {
        @SuppressWarnings("unchecked")
        HashSet<String> tmpStaging = (HashSet<String>) _staging.clone();
        for (String s : tmpStaging) {
            commit.getTrackedFiles().add(s);
            File from = new File(".gitlet/staging/" + s);
            String code = Utils.readContentsAsString(from);
            code += new Random().toString();
            String blobId = Utils.sha1(code);
            File to = new File(".gitlet/blobs/" + blobId);
            Utils.writeContents(to, Utils.readContents(from));
            commit.getFileWithBlob().put(s, blobId);

            _staging.remove(s);
            from.delete();
        }

        for (String s : _rmTmp) {
            commit.getTrackedFiles().remove(s);
            commit.getFileWithBlob().remove(s);
        }
        _rmTmp.clear();

        for (String s : _remove) {
            commit.getFileWithBlob().remove(s);
            _remove.remove(s);
        }

        commit.setId();
        if (_branchCommits.get(_currentBranch) == null) {
            LinkedList<Commit> com = new LinkedList<>();
            com.add(commit);
            _branchCommits.put(_currentBranch, com);
        } else {
            _branchCommits.get(_currentBranch).add(commit);
        }
        _allCommits.put(commit.getId(), commit);
        _head = commit;
    }

    /**
     * Remove from staging and dir.
     *
     * @param filename filename.
     */
    public void remove(String filename) {
        String[] name = filename.split("/");
        String pureFilename = name[name.length - 1];

        _remove.add(pureFilename);

        if (!_staging.contains(pureFilename)
                && !_head.getTrackedFiles().contains(pureFilename)) {
            System.out.println("No reason to remove the file.");
        }

        if (_staging.contains(pureFilename)) {
            _staging.remove(pureFilename);
            File file = new File(".gitlet/staging/" + pureFilename);
            file.delete();
        }
        if (_head.getTrackedFiles().contains(pureFilename)) {
            _rmTmp.add(pureFilename);
            File f = new File(filename);
            if (f.exists()) {
                f.delete();
            }
        }
    }

    /**
     * Show the log from _head.
     */
    public void log() {
        StringBuilder sb = new StringBuilder();
        Commit commit = _head;

        while (commit != null) {
            sb.append("===");
            sb.append("\n");
            sb.append("commit ");
            sb.append(commit.getId());
            sb.append("\n");
            if (commit.getParent() != null
                    && commit.getParent().size() == 2) {
                sb.append("Merge: ");
                sb.append(commit.getParent().get(0).getId(), 0, 7);
                sb.append(" ");
                sb.append(commit.getParent().get(1).getId(), 0, 7);
                sb.append("\n");
            }
            sb.append("Date: ");
            sb.append(commit.getTime());
            sb.append("\n");
            sb.append(commit.getMsg());
            sb.append("\n");
            sb.append("\n");
            if (commit.getParent() != null) {
                commit = commit.getParent().getFirst();
            } else {
                commit = null;
            }
        }
        System.out.println(sb.toString());
    }

    /**
     * Show all the log from all commits.
     */
    public void globalLog() {
        StringBuilder sb = new StringBuilder();

        ListIterator<Map.Entry<String, Commit>> i =
                new ArrayList<>(_allCommits.entrySet()).
                        listIterator(_allCommits.size());

        while (i.hasPrevious()) {
            Map.Entry<String, Commit> entry = i.previous();
            Commit commit = entry.getValue();
            sb.append("===");
            sb.append("\n");
            sb.append("commit ");
            sb.append(commit.getId());
            sb.append("\n");
            if (commit.getParent() != null
                    && commit.getParent().size() == 2) {
                sb.append("Merge: ");
                sb.append(commit.getParent().get(0).getId(), 0, 7);
                sb.append(" ");
                sb.append(commit.getParent().get(1).getId(), 0, 7);
                sb.append("\n");
            }
            sb.append("Date: ");
            sb.append(commit.getTime());
            sb.append("\n");
            sb.append(commit.getMsg());
            sb.append("\n");
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    /**
     * Find the commit ids with particular commit msg.
     *
     * @param msg msg.
     */
    public void find(String msg) {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (String key : _allCommits.keySet()) {
            if (_allCommits.get(key).getMsg().equals(msg)) {
                sb.append(_allCommits.get(key).getId());
                sb.append("\n");
                count++;
            }
        }
        if (count == 0) {
            System.err.println("Found no commit with that message.");
            return;
        }
        System.out.println(sb.toString());
    }

    /**
     * Print out status.
     */
    public void status() {
        if (_head == null) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }

        StringBuilder sb = new StringBuilder();

        sb.append("=== Branches ===");
        sb.append("\n");
        Set<String> keys = _branchCommits.keySet();
        List<String> sortedKeys = new ArrayList<>(keys);
        Collections.sort(sortedKeys);
        for (String key : sortedKeys) {
            if (key.equals(_currentBranch)) {
                sb.append("*");
                sb.append(key);
                sb.append("\n");
            } else {
                sb.append(key);
                sb.append("\n");
            }
        }
        sb.append("\n");

        sb.append("=== Staged Files ===");
        sb.append("\n");
        List<String> sortedfiles = new ArrayList<>(_staging);
        Collections.sort(sortedfiles);
        for (String s : sortedfiles) {
            sb.append(s);
            sb.append("\n");
        }
        sb.append("\n");

        sb.append("=== Removed Files ===");
        sb.append("\n");
        List<String> sortedRemove = new ArrayList<>(_remove);
        Collections.sort(sortedRemove);
        for (String s : sortedRemove) {
            if (!new File(s).exists()) {
                sb.append(s);
                sb.append("\n");
            }
        }
        sb.append("\n");

        sb.append("=== Modifications Not Staged For Commit ===");
        sb.append("\n");
        sb.append("\n");

        sb.append("=== Untracked Files ===");
        sb.append("\n");
        sb.append("\n");

        System.out.println(sb.toString());
    }

    /**
     * 1. checkout [branch name]:
     * Create/overwrite files, update _currentBranch & _head.
     * 2. checkout -- [file name]: Write out file.
     * 3. checkout [commit id] -- [file name]: Create/overwrite files.
     *
     * @param args args.
     */
    public void checkout(String[] args) {
        if (args.length == 2) {
            subCheckout(args);
        } else if (args.length == 3) {
            Commit commit = _head;
            if (!commit.getFileWithBlob().containsKey(args[2])) {
                System.out.println("File does not exist in that commit.");
                return;
            }
            String id = commit.getFileWithBlob().get(args[2]);
            byte[] b = Utils.readContents(new File(".gitlet/blobs/" + id));
            Utils.writeContents(new File(args[2]), b);
        } else if (args.length == 4) {
            if (!args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                return;
            }
            String cid = args[1];
            Set<String> keys = _allCommits.keySet();
            Commit commit = new Commit();
            boolean find = false;
            for (String key : keys) {
                if (cid.equals(key.substring(0, cid.length()))) {
                    commit = _allCommits.get(key);
                    find = true;
                    break;
                }
            }
            if (!find) {
                System.out.println("No commit with that id exists.");
                return;
            }
            if (!commit.getFileWithBlob().containsKey(args[3])) {
                System.out.println("File does not exist in that commit.");
                return;
            }
            String id = commit.getFileWithBlob().get(args[3]);
            byte[] b = Utils.readContents(new File(".gitlet/blobs/" + id));
            Utils.writeContents(new File(args[3]), b);
        } else {
            return;
        }
    }

    /**
     * subCheckout.
     *
     * @param args args.
     */
    public void subCheckout(String[] args) {
        if (args[1].equals(_currentBranch)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        if (!_branchCommits.keySet().contains(args[1])) {
            System.out.println("No such branch exists.");
            return;
        }
        Commit commit = _branchCommits.get(args[1]).getLast();
        for (String s : commit.getFileWithBlob().keySet()) {
            if (!_head.getTrackedFiles().contains(s)
                    && new File(s).exists()) {
                System.out.println("There is an untracked "
                        + "file in the way; delete it or add it first.");
                return;
            }
        }

        for (String s : _head.getTrackedFiles()) {
            if (!commit.getFileWithBlob().keySet().contains(s)) {
                new File(s).delete();
            }
        }

        _currentBranch = args[1];
        for (String s : commit.getFileWithBlob().keySet()) {
            String id = commit.getFileWithBlob().get(s);
            byte[] b = Utils.readContents(new File(".gitlet/blobs/" + id));
            Utils.writeContents(new File(s), b);
        }
        _head = commit;
    }

    /**
     * Creat new branch, add _head to the branch head.
     *
     * @param args args.
     */
    public void branch(String[] args) {
        String branchName = args[1];

        if (_branchCommits.keySet().contains(branchName)) {
            System.out.println("A branch with that name already exsists.");
            return;
        }

        LinkedList<Commit> com = new LinkedList<>();
        com.add(_head);
        Commit tmp = _head;
        while (tmp.getParent() != null) {
            tmp = tmp.getParent().getFirst();
            com.addFirst(tmp);
        }
        _branchCommits.put(branchName, com);
    }

    /**
     * Remove branch, should not remove its commits.
     *
     * @param branch branch.
     */
    public void rmBranch(String branch) {
        if (!_branchCommits.keySet().contains(branch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (_currentBranch.equals(branch)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        _branchCommits.remove(branch);
    }

    /**
     * Reset.
     *
     * @param id id.
     */
    public void reset(String id) {
        Set<String> keys = _allCommits.keySet();
        Commit commit = new Commit();
        boolean find = false;
        for (String key : keys) {
            if (id.equals(key.substring(0, id.length()))) {
                commit = _allCommits.get(key);
                find = true;
                break;
            }
        }
        if (!find) {
            System.out.println("No commit with that id exists.");
            return;
        }
        for (String s : commit.getFileWithBlob().keySet()) {
            if (!_head.getTrackedFiles().contains(s)
                    && new File(s).exists()) {
                System.out.println("There is an untracked file"
                        + " in the way; delete it or add it first.");
                return;
            }
        }

        for (String fileName : commit.getTrackedFiles()) {
            String blobid = commit.getFileWithBlob().get(fileName);
            if (blobid != null) {
                byte[] b = Utils.readContents
                        (new File(".gitlet/blobs/" + blobid));
                Utils.writeContents(new File(fileName), b);
            } else {
                File file = new File(fileName);
                file.delete();
            }
        }

        _head = commit;
        for (String branch : _branchCommits.keySet()) {
            LinkedList<Commit> commits = _branchCommits.get(branch);
            if (commits.contains(_head)) {
                _currentBranch = branch;
                break;
            }
        }
    }

    /**
     * Megre.
     *
     * @param branch branch.
     */
    public void merge(String branch) {
        if (!_staging.isEmpty() || !_remove.isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        if (!_branchCommits.keySet().contains(branch)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        if (branch.equals(_currentBranch)) {
            System.out.println("Cannot merge a branch with itself");
            return;
        }
        boolean conflicted = false;
        Commit splitPoint = new Commit();

        LinkedList<Commit> target = _branchCommits.get(branch);
        LinkedList<Commit> curr = _branchCommits.get(_currentBranch);
        for (Commit cmt : target) {
            if (!curr.contains(cmt)) {
                int index = target.indexOf(cmt);
                if (index > 0) {
                    splitPoint = target.get(index - 1);
                } else {
                    splitPoint = target.get(0);
                }
                break;
            }
        }
        HashMap<String, String> spFiles = splitPoint.getFileWithBlob();
        HashMap<String, String> targetFiles =
                _branchCommits.get(branch).getLast().getFileWithBlob();
        HashMap<String, String> currFiles =
                _branchCommits.get(_currentBranch).getLast().getFileWithBlob();

        conflicted = checkTarget(branch, targetFiles, currFiles, spFiles);
        conflicted = checkSp(conflicted, targetFiles, currFiles, spFiles);
        for (String currFile : currFiles.keySet()) {
            if (!spFiles.containsKey(currFile)
                    && !targetFiles.containsKey(currFile)) {
                if (currFile.equals("f.txt")) {
                    new File(currFile).delete();
                }
            }
        }

        if (_staging.size() == 0 && _remove.size() == 0) {
            System.out.println("No changes added to the commit.");
            return;
        }
        Commit mergeCom = new Commit(_branchCommits.
                get(_currentBranch).getLast(),
                _branchCommits.get(branch).getLast(),
                "Merged " + branch + " into " + _currentBranch + ".");
        mergeCommit(mergeCom);
        if (conflicted) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /**
     * Check Target.
     *
     * @param branch      branch.
     * @param targetFiles targetFiles.
     * @param currFiles   currFiles.
     * @param spFiles     spFiles.
     * @return Conflicted.
     */
    private boolean checkTarget(String branch,
                                HashMap<String, String> targetFiles,
                                HashMap<String, String> currFiles,
                                HashMap<String, String> spFiles) {
        for (String targetFile : targetFiles.keySet()) {
            if (spFiles.containsKey(targetFile)
                    && !targetFiles.get(targetFile).
                    equals(spFiles.get(targetFile))
                    && !currFiles.containsKey(targetFile)) {

                mergeStage(targetFile, targetFiles);

            } else if (!spFiles.containsKey(targetFile)
                    && !currFiles.containsKey(targetFile)) {
                _head = _branchCommits.get(branch).getLast();
                checkout(new String[]{"checkout", "--", targetFile});
                _head = _branchCommits.get(_currentBranch).getLast();
                mergeStage(targetFile, targetFiles);
            } else if (!spFiles.containsKey(targetFile)
                    && !currFiles.get(targetFile).
                    equals(targetFiles.get(targetFile))) {

                String currContent = Utils.readContentsAsString
                        (new File(".gitlet/blobs/"
                                + currFiles.get(targetFile)));
                String givenContent = Utils.readContentsAsString
                        (new File(".gitlet/blobs/"
                                + targetFiles.get(targetFile)));
                Utils.writeContents(new File(targetFile),
                        content(currContent, givenContent));
                return true;
            }
        }
        return false;
    }

    /**
     * Check Target.
     * @param con  conflicted.
     * @param targetFiles targetFiles.
     * @param currFiles   currFiles.
     * @param spFiles     spFiles.
     * @return Conflicted.
     */
    private boolean checkSp(boolean con,
                            HashMap<String, String> targetFiles,
                            HashMap<String, String> currFiles,
                            HashMap<String, String> spFiles) {
        for (String spFile : spFiles.keySet()) {
            if (!targetFiles.containsKey(spFile)
                    && currFiles.containsKey(spFile)
                    && currFiles.get(spFile).equals(spFiles.get(spFile))) {
                new File(spFile).delete();
            } else if (targetFiles.containsKey(spFile)
                    && currFiles.containsKey(spFile)
                    && !currFiles.get(spFile).equals(targetFiles.get(spFile))) {

                String currContent = Utils.readContentsAsString
                        (new File(".gitlet/blobs/" + currFiles.get(spFile)));
                String givenContent = Utils.readContentsAsString
                        (new File(".gitlet/blobs/" + targetFiles.get(spFile)));
                Utils.writeContents(new File(spFile),
                        content(currContent, givenContent));
                return true;

            } else if (!targetFiles.containsKey(spFile)
                    && currFiles.containsKey(spFile)
                    && !currFiles.get(spFile).equals(spFiles.get(spFile))) {
                String currContent = Utils.readContentsAsString
                        (new File(".gitlet/blobs/" + currFiles.get(spFile)));
                String givenContent = "";
                Utils.writeContents(new File(spFile),
                        content(currContent, givenContent));
                return true;
            } else if (targetFiles.containsKey(spFile)
                    && !currFiles.containsKey(spFile)
                    && !targetFiles.get(spFile).equals(spFiles.get(spFile))) {
                String currContent = "";
                String givenContent = Utils.readContentsAsString
                        (new File(".gitlet/blobs/" + targetFiles.get(spFile)));
                Utils.writeContents(new File(spFile),
                        content(currContent, givenContent));
                return true;
            }
        }
        return con;
    }

    /**
     * Merge and stage.
     *
     * @param targetFile  targetFile.
     * @param targetFiles targetFiles.
     */
    public void mergeStage(String targetFile,
                           HashMap<String, String> targetFiles) {
        String[] name = targetFile.split("/");
        String pureFilename = name[name.length - 1];

        if (_remove.contains(pureFilename)) {
            _remove.remove(pureFilename);
        }

        File oldFile = new File(".gitlet/blobs/" + targetFiles.get(targetFile));
        File newFile = new File(".gitlet/staging/" + pureFilename);
        String s1 = Utils.readContentsAsString(oldFile);
        if (_head.getBlobs().size() != 0) {
            for (String blob : _head.getBlobs()) {
                File f = new File(".gitlet/blobs/" + blob);
                String s2 = Utils.readContentsAsString(f);
                if (s1.equals(s2) && newFile.exists()) {
                    newFile.delete();
                    _staging.remove(pureFilename);
                }
            }
        }

        Utils.writeContents(newFile, Utils.readContents(oldFile));
        _staging.add(pureFilename);
        _head.getTrackedFiles().add(pureFilename);
    }

    /**
     * Return merge conflict file.
     *
     * @param curr  curr.
     * @param given given.
     * @return String.
     */
    private String content(String curr, String given) {
        String s = "<<<<<<< HEAD"
                + "\n"
                + curr
                + "======="
                + "\n"
                + given
                + ">>>>>>>"
                + "\n";
        return s;
    }

    /**
     * Get untracked Files.
     *
     * @param tracked tracked.
     * @return List.
     */
    @SuppressWarnings("unchecked")
    public List<String> getUntrack(HashSet tracked) {
        List<String> l = Utils.plainFilenamesIn(new File("../proj3"));
        ArrayList<String> list = new ArrayList<>();
        ArrayList<String> t = new ArrayList<>();
        t.addAll(tracked);
        list.addAll(l);
        list.remove(".gitignore");
        list.remove("Makefile");
        list.remove("gitlet-design.txt");
        list.remove("proj3.iml");
        list.remove(t);
        return list;
    }

    /**
     * Record current branch.
     */
    private String _currentBranch;
    /**
     * Current Commit.
     */
    private Commit _head;
    /**
     * All of the commits paired with Branch.
     */
    private HashMap<String, LinkedList<Commit>> _branchCommits;
    /**
     * All commits.
     */
    private LinkedHashMap<String, Commit> _allCommits;
    /**
     * File names to be removed.
     */
    private HashSet<String> _remove;
    /**
     * Staging file names.
     */
    private HashSet<String> _staging;
    /**
     * Remove tmp.
     */
    private HashSet<String> _rmTmp;

}
