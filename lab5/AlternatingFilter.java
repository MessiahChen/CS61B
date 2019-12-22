import java.util.Iterator;

import utils.Filter;

/**
 * A kind of Filter that lets through every other VALUE element of
 * its input sequence, starting with the first.
 *
 * @author Your Name
 */
class AlternatingFilter<Value> extends Filter<Value> {

    /**
     * A filter of values from INPUT that lets through every other
     * value.
     */
    AlternatingFilter(Iterator<Value> input) {
        super(input);
        this._input = input;
        this._valid = false;
    }

    @Override
    protected boolean keep() {
        if (!_first && !_valid && _input.hasNext()) {
            _next = _input.next();
            _first = false;
            return _next != null;
        } else if (!_input.hasNext()) {
            return false;
        }
        _first = false;
        return _next != null;
    }

    private Iterator<Value> _input;
    private boolean _valid;
    private boolean _first = true;
}