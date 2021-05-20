package com.apigate.exceptions.db;

import com.apigate.exceptions.AbstractException;
import com.apigate.exceptions.OperationResultWarnLevel;
import com.apigate.exceptions.ResponseCodes;
import com.apigate.logging.Logger;
import com.apigate.logging.ServicesLog;
import org.springframework.http.HttpStatus;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
public class RecordNotFoundException extends AbstractException implements OperationResultWarnLevel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final ResponseCodes.Warns warn = ResponseCodes.Warns.CIS0004;

	private RecordNotFoundException(){
		super(HttpStatus.NOT_FOUND);
	}

	public RecordNotFoundException(String resourceId) {
		super(HttpStatus.NOT_FOUND,warn + " : " + resourceId);
	}

	@Override
	public Logger getLogger() {
		return ServicesLog.getInstance();
	}

	@Override
	public void log() {
		this.getLogger().logWarn(this.getMessage());
	}


	@Override
	public ResponseCodes.Warns getWarnResponseCode() {
		return warn;
	}
}
