package org.avmframework.examples.util;

import org.apache.commons.lang3.StringUtils;
import org.avmframework.localsearch.LocalSearch;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArgsParser {

  protected Class<?> classWithMainMethod;
  protected String[] args;
  protected Map<String, String> params = new LinkedHashMap<>();

  public ArgsParser(Class<?> classWithMainMethod, String[] args) {
    this.classWithMainMethod = classWithMainMethod;
    this.args = args;
    addParams();
  }

  protected void addParams() {
    addParam(
        "[search]",
        "an optional parameter denoting which search to use "
            + "(e.g., \"IteratedPatternSearch\", \"GeometricSearch\" or \"LatticeSearch\")");
  }

  public void addParam(String name, String description) {
    params.put(name, description);
  }

  public void error(String error) {
    System.out.println("ERROR: " + error);
    usage();
    System.exit(1);
  }

  public void usage() {
    String usage =
        "USAGE: java "
            + classWithMainMethod
            + " "
            + StringUtils.join(params.keySet(), " ")
            + " \n"
            + "  where: \n";
    for (String paramName : params.keySet()) {
      usage += "    - " + paramName + " is " + params.get(paramName) + "\n";
    }

    System.out.println(usage);
  }

  public LocalSearch parseSearchParam(String defaultSearch) {
    return parseSearchParam(0, defaultSearch);
  }

  public LocalSearch parseSearchParam(int index, String defaultSearch) {
    String searchName = defaultSearch;
    if (args.length > index) {
      searchName = args[index];
    }

    LocalSearch search = null;
    try {
      search = LocalSearch.instantiate(searchName);
    } catch (Exception exception) {
      error(exception.getMessage());
    }

    return search;
  }
}
