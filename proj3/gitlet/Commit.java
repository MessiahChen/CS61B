package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Commit Class.
 * @author Mingjie Chen.
 */
public class Commit implements Serializable {
    /**
     * SHA-1 id.
     */
    private String _id;
    /**
     * Commit message.
     */
    private String _msg;
    /**
     * Commit time.
     */
    private String _time;
    /**
     * Parents.
     */
    private LinkedList<Commit> _parent;
    /**
     * Tracked Files.
     */
    private HashSet<String> _trackedFiles;
    /**
     * File name and blob ID.
     */
    private HashMap<String, String> _fileWithBlob;

    /**
     * Default Constructor.
     */
    public Commit() {
        _msg = "initial commit";
        _time = new SimpleDateFormat("EEE MMM dd"
                + " HH:mm:ss yyyy Z").format(new Date(0));
        _parent = null;
        _fileWithBlob = new HashMap<>();
        _trackedFiles = new HashSet<>();
        _id = getHash();
    }


    /**
     * Constructor.
     * @param cmt commit.
     * @param msg message.
     */
    @SuppressWarnings("unchecked")
    public Commit(Commit cmt, String msg) {
        _msg = msg;
        _time = new SimpleDateFormat("EEE MMM dd"
                + " HH:mm:ss yyyy Z").format(new Date());
        _parent = new LinkedList<>();
        _parent.add(cmt);
        _fileWithBlob = (HashMap<String, String>) cmt.getFileWithBlob().clone();
        _trackedFiles = (HashSet<String>) cmt.getTrackedFiles().clone();
        _id = getHash();
    }

    /**
     * Constructor.
     * @param cmt1 commit1.
     * @param cmt2 commit2.
     * @param msg message.
     */
    @SuppressWarnings("unchecked")
    public Commit(Commit cmt1, Commit cmt2, String msg) {
        _msg = msg;
        _time = new SimpleDateFormat("EEE MMM dd "
                + "HH:mm:ss yyyy Z").format(new Date());
        _parent = new LinkedList<>();
        _parent.add(cmt1);
        _parent.add(cmt2);
        _fileWithBlob = (HashMap<String, String>)
                cmt1.getFileWithBlob().clone();
        for (String key : cmt2.getFileWithBlob().keySet()) {
            _fileWithBlob.put(key, cmt2.getFileWithBlob().get(key));
        }
        _trackedFiles = (HashSet<String>) cmt1.getTrackedFiles().clone();
        for (String f : cmt2.getTrackedFiles()) {
            _trackedFiles.add(f);
        }
        _id = getHash();
    }

    /**
     * Set id.
     */
    public void setId() {
        this._id = getHash();
    }

    /**
     * get id.
     * @return
     */
    public String getId() {
        return _id;
    }

    /**
     * Get message.
     * @return String msg.
     */
    public String getMsg() {
        return _msg;
    }

    /**
     * Get time.
     * @return String time.
     */
    public String getTime() {
        return _time;
    }

    /**
     * Get Parents.
     * @return LinkedList parents.
     */
    public LinkedList<Commit> getParent() {
        return _parent;
    }

    /**
     * Get FileWithBlob.
     * @return HashMap.
     */
    public HashMap<String, String> getFileWithBlob() {
        return _fileWithBlob;
    }

    /**
     * Get tracked Files.
     * @return Hashset.
     */
    public HashSet<String> getTrackedFiles() {
        return _trackedFiles;
    }

    /**
     * Get blobs.
     * @return HashSet.
     */
    public HashSet<String> getBlobs() {
        HashSet<String> res = new HashSet<>();
        if (_fileWithBlob != null) {
            for (String key : _fileWithBlob.keySet()) {
                res.add(_fileWithBlob.get(key));
            }
        }
        return res;
    }

    /**
     * Calculate SHA-1 Code.
     * @return String.
     */
    public String getHash() {
        String s = null;
        s += _time;
        s += _msg;
        s += _parent;
        if (_fileWithBlob != null) {
            for (String key : _fileWithBlob.keySet()) {
                File f = new File(".gitlet/blobs/" + _fileWithBlob.get(key));
                String res = Utils.readContentsAsString(f);
                s += res;
            }
        }
        return Utils.sha1(s);
    }
}
