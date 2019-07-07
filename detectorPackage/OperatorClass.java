package detectorPackage;

public enum OperatorClass {

	LESSTHAN(){

		@Override
		public boolean isLessThan(int var1, int var2) {

			return var1 <= var2;
		}


	},// end less than

	GREATERTHAN(){

		@Override
		public boolean isGreaterThan(int var1, int var2) {

			return var1 >= var2;
		}

	};// end greater than

	public boolean isLessThan(int var1, int var2) {
		return var1 <= var2;
	}

	public boolean isGreaterThan(int var1, int var2) {
		return var1 >= var2;
	}

}// end OperatorClass


