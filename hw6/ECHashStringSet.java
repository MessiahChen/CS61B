import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A set of String values.
 *
 * @author Mingjie Chen
 */
class ECHashStringSet implements StringSet {

    public ECHashStringSet() {
        _size = 0;
        _table = new LinkedList[5];
    }

    @Override
    public void put(String s) {
        // FIXME
        if (_table.length != 0 && (double) _size / (double) _table.length > 5) {
            resize();
        }
        int index = s.hashCode() % _table.length;
        if (index < 0) {
            index = index & 0x7fffffff % _table.length;
        }
        while (index > _table.length) {
            resize();
        }
        if (_table[index] == null) {
            _table[index] = new LinkedList<>();
        }
        _table[index].add(s);
        _size++;

    }

    @Override
    public boolean contains(String s) {
        // FIXME
        int index = s.hashCode() % _table.length;
        if (index < 0) {
            index = index & 0x7fffffff % _table.length;
        }
        if (_table[index] != null && _table[index].contains(s)) {
            return true;
        }
        return false;
    }

    @Override
    public List<String> asList() {
        // FIXME
        ArrayList<String> res = new ArrayList<>();
        for (LinkedList<String> al : _table) {
            if (al != null) {
                for (String s : al) {
                    res.add(s);
                }
            }
        }
        return res;
    }

    public void resize() {
        LinkedList<String>[] old = _table;
        _table = new LinkedList[2 * old.length];
        _size = 0;
        for (LinkedList<String> al : old) {
            if (al != null) {
                for (String s : al) {
                    this.put(s);
                }
            }
        }
    }

    private LinkedList<String>[] _table;
    private int _size;

}
