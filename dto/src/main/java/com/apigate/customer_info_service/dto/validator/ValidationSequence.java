package com.apigate.customer_info_service.dto.validator;

import javax.validation.GroupSequence;

/**
 * @author Bayu Utomo
 * @date 4/12/2020 10:27 PM
 */
@GroupSequence({Step1.class, Step2.class, Step3.class})
public interface ValidationSequence {
}
