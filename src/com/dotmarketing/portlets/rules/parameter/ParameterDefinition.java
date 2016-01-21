package com.dotmarketing.portlets.rules.parameter;

import com.dotcms.repackage.com.google.common.base.Preconditions;
import com.dotcms.repackage.org.apache.commons.lang.StringUtils;
import com.dotcms.rest.exception.InvalidConditionParameterException;
import com.dotmarketing.portlets.rules.exception.RuleEngineException;
import com.dotmarketing.portlets.rules.model.ParameterModel;
import com.dotmarketing.portlets.rules.parameter.display.Input;
import com.dotmarketing.portlets.rules.parameter.type.DataType;
import com.dotmarketing.util.Logger;

public class ParameterDefinition<T extends DataType> {

    private final String key;
    private final String i18nBaseKey;
    private final String defaultValue;
    private final Input<T> inputType;
    private final int priority;

    /**
     * Creates a parameter with the key, data type and a default value
     */
    public ParameterDefinition(int priority, String key, Input<T> inputType) {
        this(priority, key, inputType, "");
    }

    public ParameterDefinition(int priority, String key, Input<T> inputType, String defaultValue) {
        this(priority, key, null, inputType, defaultValue);
    }
    public ParameterDefinition(int priority, String key, String i18nBaseKey, Input<T> inputType, String defaultValue) {
        Preconditions.checkState(StringUtils.isNotBlank(key), "ParameterDefinition requires a valid key.");
        this.key = key;
        this.i18nBaseKey = i18nBaseKey;
        this.defaultValue = defaultValue == null ? "" : defaultValue;
        this.inputType = inputType;
        this.priority = priority;
    }

    public String getKey() {
        return key;
    }

    public String getI18nBaseKey() {
        return i18nBaseKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public int getPriority() {
        return priority;
    }

    public Input<T> getInputType() {
        return inputType;
    }

    public void checkValid(ParameterModel model) throws InvalidConditionParameterException, RuleEngineException {
    	try{
            this.inputType.getDataType().checkValid(model.getValue());
            // check for valid parameters
            this.inputType.checkValid(model.getValue());
            model.getValue();
    	}
    	catch(InvalidConditionParameterException ip){
    		throw ip;
    	}
    	catch(Exception e){
    		Logger.error(this.getClass(), e.getMessage(), e);
    		throw new RuleEngineException(e.getMessage());
    	}

    }
}

