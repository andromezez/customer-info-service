package com.apigate.utils.masking;


import com.apigate.customer_info_service.dto.httpresponsebody.masking.MaskingEntryDto;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import java.util.List;


/**
 * @author Shehan silva
 * @date 25/07/2021 10:27 PM
 */
public class ResponseMaskingUtils {
    
	/*
	 * @requestParam clientMaskingConfigList : Json path list to be masked for the client
	 * @requestParam maskingValue : Masking param taken from the properties
	 * @requestParam reseponseBody : response generated from backend 
	 * 
	 * */
    public static String responseMasking(List<MaskingEntryDto> clientMaskingConfigList, 
    		String maskingValue,String reseponseBody){
    	
    	Configuration conf = Configuration.builder()
    		     .options(Option.AS_PATH_LIST).build();
    	
    	DocumentContext json = JsonPath.using(conf).parse(reseponseBody);    	
    	
    	for (int i = 0; i < clientMaskingConfigList.size(); i++) {			
    		
    		boolean atResponse = clientMaskingConfigList.get(i).isAtResponse();
    		
    		if(atResponse) {
    			
    			String maskingParamPath = clientMaskingConfigList.get(i).getJsonPath();        		
        		List<String> jsonPathListInReponse = JsonPath.parse(json).read(maskingParamPath);
        		
    			if(jsonPathListInReponse.size()>=0) {
    				json.set(maskingParamPath, maskingValue);
    			}
        		
    		}
    		
		}
    	
    	return json.jsonString();
    }
    
    
}
