package consoleupgrade;

public class ConsoleUpgrade {

public  ConsoleUpgrade(){
	System.setOut(new java.io.PrintStream(System.out) {

		private StackTraceElement getCallSite() {
			for (StackTraceElement e : Thread.currentThread()
																 .getStackTrace())
				if (!e.getMethodName().equals("getStackTrace")
						&& !e.getClassName().equals(getClass().getName()))
					return e;
			return null;
		}

		@Override
		public void println(String s) {
			println((Object) s);
		}

		@Override
		public void println(Object o) {
			StackTraceElement e = getCallSite();
			String callSite = e == null ? "??" :
												String.format("%s.%s(%s:%d)",
												e.getClassName(),
												e.getMethodName(),
												e.getFileName(),
												e.getLineNumber());
			super.println(o + "\t\tat " + callSite);
		}

		@Override
		public void println(int x) {
			StackTraceElement e = getCallSite();
			String callSite = e == null ? "??" :
												String.format("%s.%s(%s:%d)",
												e.getClassName(),
												e.getMethodName(),
												e.getFileName(),
												e.getLineNumber());
			super.println(x + "\t\tat " + callSite);
		}
	});

	System.setErr(new java.io.PrintStream(System.err) {

		private StackTraceElement getCallSite() {
			for (StackTraceElement e : Thread.currentThread()
																 .getStackTrace())
				if (!e.getMethodName().equals("getStackTrace")
						&& !e.getClassName().equals(getClass().getName()))
					return e;
			return null;
		}

		@Override
		public void println(String s) {
			println((Object) s);
		}

		@Override
		public void println(Object o) {
			StackTraceElement e = getCallSite();
			String callSite = e == null ? "??" :
												String.format("%s.%s(%s:%d)",
												e.getClassName(),
												e.getMethodName(),
												e.getFileName(),
												e.getLineNumber());
			super.println(o + "\t\tat " + callSite);
		}


		@Override
		public void println(int x) {
			StackTraceElement e = getCallSite();
			String callSite = e == null ? "??" :
												String.format("%s.%s(%s:%d)",
												e.getClassName(),
												e.getMethodName(),
												e.getFileName(),
												e.getLineNumber());
			super.println(x + "\t\tat " + callSite);
		}
	});
}
}

