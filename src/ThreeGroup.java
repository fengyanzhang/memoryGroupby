
public class ThreeGroup<F, S, T> implements SubGroup<F, S, T> {

	private F f;

	private S s;

	private T t;

	public ThreeGroup() {

	}

	public ThreeGroup(F f, S s, T t) {
		this.f = f;
		this.s = s;
		this.t = t;
	}

	@Override
	public F getGroupKey() {
		return f;
	}

	@Override
	public S getGroup() {
		return s;
	}

	@Override
	public T getGroupValue() {
		return t;
	}

}
