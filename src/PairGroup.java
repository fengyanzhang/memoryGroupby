
public class PairGroup<F, S, T> implements SubGroup<F,S, T> {

	private F f;

	private T t;

	@Override
	public F getGroupKey() {
		return f;
	}

	@Override
	public T getGroupValue() {
		return t;
	}
	

	@Override
	public S getGroup() {
		return null;
	}

	public PairGroup() {

	}

	public PairGroup(F f, S s, T t) {
		this.f = f;
		this.t = t;
	}

}
