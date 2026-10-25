# Common Module of Exasol Virtual Schemas Adapters 18.1.1, released 2026-??-??

Code name: Fixed vulnerability CVE-2026-110974 in com.vaadin.external.google:android-json:jar:0.0.20131108.vaadin1:test

## Summary

This release fixes the following vulnerability:

### CVE-2026-110974 (CWE-674) in dependency `com.vaadin.external.google:android-json:jar:0.0.20131108.vaadin1:test`
The org.json parser in com.vaadin.external.google:android-json recurses once per nesting level with no depth limit: JSONTokener.nextValue() calls readArray() and readObject(), which call nextValue() again. A deeply nested JSON document, around 20 KB of nested arrays with the default thread stack, makes the JSONArray and JSONObject constructors throw a raw java.lang.StackOverflowError instead of a JSONException. Any application that parses untrusted JSON with this library can be made to fail the thread doing the parsing. The artifact is a 2013 repackage of third-party org.json classes, published once and never maintained, and no fixed version will be released.

Users of the affected version should apply the following mitigation.

Product version
com.vaadin.external.google:android-json 0.0.20131108.vaadin1

Mitigation
No fixed version will be released. Replace the artifact with an org.json implementation that limits the nesting depth, such as org.json:json 20231013 or later.

Artifacts
Maven coordinates / Vulnerable versions / Fixed version
com.vaadin.external.google:android-json / 0.0.20131108.vaadin1 / none
#### References
* https://guide.sonatype.com/vulnerability/CVE-2026-110974?component-type=maven&component-name=com.vaadin.external.google%2Fandroid-json&utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1
* http://web.nvd.nist.gov/view/vuln/detail?vulnId=CVE-2026-110974
* https://github.com/CVEProject/cvelistV5/blob/main/cves/2026/110xxx/CVE-2026-110974.json

## Security

* #343: Fixed vulnerability CVE-2026-110974 in dependency `com.vaadin.external.google:android-json:jar:0.0.20131108.vaadin1:test`

## Dependency Updates

### Test Dependency Updates

* Updated `nl.jqno.equalsverifier:equalsverifier:3.19.4` to `4.5.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.14.4` to `6.1.3`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:5.7.7` to `5.8.1`
