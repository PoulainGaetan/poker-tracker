package dsia.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) {
        final Pattern pattern = Pattern.compile("<" + "Result" + "\\s+(.*?)\\s*/>");
        final Pattern attributesPattern = Pattern.compile("(.+?)=\"(.*?)\"\\s*");
//        final Matcher matcher = pattern.matcher("<ALL_XML_OUT>\r\n" + 
//        		"			<Result RC=\"77001\" ErrorLabel=\"Lowering a lock must follow this steps :\r\n" + 
//        		"- Unlock (status 0) with the previous reason.\r\n" + 
//        		"- Then, lock with the new reason.\" />\r\n" + 
//        		"			</ALL_XML_OUT>");
        final Matcher matcher = pattern.matcher("<ALL_XML_OUT><Result RC=\"77001\" ErrorLabel=\"Lowering a lock must follow this steps\" /></ALL_XML_OUT>");

        final List<Map<String, String>> maps = new ArrayList<>();

        while (matcher.find()) {
            final Map<String, String> map = new HashMap<>();
            final String content = matcher.group(1);
            final Matcher attributesMapper = attributesPattern.matcher(content);

            while (attributesMapper.find()) {
                map.put(attributesMapper.group(1), attributesMapper.group(2));
            }

            maps.add(map);
        }
	}

}
