package musictheory;

public interface Loadable {
	void load(int iterations);

	void start() throws NotLoadedException;

	static class NotLoadedException extends Exception {
		@Override
		public String toString() {
			return "The Loadable you were trying to start had not been loaded";
		}
	}
}
