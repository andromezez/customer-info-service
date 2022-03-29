package com.apigate.utils.masking;


import com.apigate.logging.ServicesLog;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import java.util.ArrayList;


/**
 * @author Shehan silva
 * @date 25/07/2021 10:27 PM
 */
public class ResponseMaskingUtils {

	/**
	 * Mask the value of the responseBody according to the specified jsonPath
	 *
	 * @param jsonPath      list of the json path
	 * @param maskingValue  the masking value
	 * @param reseponseBody the reseponse body
	 * @return the string
	 */
	public static String responseMasking(ArrayList<String> jsonPath, String maskingValue, String reseponseBody){
		jsonPath.sort((o1, o2) -> {
			if(o1.length()<o2.length()){
				return 1;
			}else if(o1.length()>o2.length()){
				return -1;
			}else{
				return 0;
			}
		});

		DocumentContext jsonCtx = JsonPath.parse(reseponseBody);

    	for (var path : jsonPath){
    		try{
    			jsonCtx.read(path,String.class); //read will force to throw error when json path is invalid.
    			jsonCtx.set(path,maskingValue); //set will not throw error when json path is invalid.
			}catch (Exception e){
				ServicesLog.getInstance().logError(e);
				ServicesLog.getInstance().logInfo("Skip the json path");
			}
		}

    	return jsonCtx.jsonString();
    }
    
    
}
