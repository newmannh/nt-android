package com.syntropy.nationaltravelandroid.exception;

import android.util.Log;

public class NTException extends Exception {

	private static final long serialVersionUID = 3678397644133332053L;

	public NTException() {	
		super();
		Log.d("ShalewaterException", "ShalewaterException()");
	}

	public NTException(String detailMessage) {
		super(detailMessage);
		Log.d("ShalewaterException", "ShalewaterException("+detailMessage+")");
	}

	public NTException(Throwable throwable) {
		super(throwable);
		Log.d("ShalewaterException", "ShalewaterException("+throwable+")");
	}

	public NTException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		Log.d("ShalewaterException", "ShalewaterException("+detailMessage+", "+throwable+")");
	}

	public NTException withLog(String tag, String text){
		Log.e(tag, text+" ["+getMessage()+"]");
		return this;
	}



}
