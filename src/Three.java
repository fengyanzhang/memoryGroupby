
public class Three<F, S, T> {

	private F f;

	private S s;

	private T t;

	public Three() {

	}

	public Three(F f, S s, T t) {
		this.f = f;
		this.s = s;
		this.t = t;
	}

	public F getF() {
		return f;
	}

	public void setF(F f) {
		this.f = f;
	}

	public S getS() {
		return s;
	}

	public void setS(S s) {
		this.s = s;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}
}
