package com.taskonline.util;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "database-string")
public class DatabaseString {
    public String  CHOICE_QUESTION;
    public String  MULTIPLE_QUESTION;
    public String  SHORT_QUESTION;
    public String  LONG_QUESTION;
    public String  ACTIVE;
    public String  UN_ACTIVE;
    public String  MARK;
    public String  UNMARK;
}
