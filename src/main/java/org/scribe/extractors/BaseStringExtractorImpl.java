package org.scribe.extractors;

import java.util.*;

import org.scribe.exceptions.*;
import org.scribe.model.*;
import org.scribe.utils.*;

public class BaseStringExtractorImpl implements BaseStringExtractor
{

  private static final String AMPERSAND_SEPARATED_STRING = "%s&%s&%s";

  public String extract(OAuthRequest request)
  {
    checkPreconditions(request);
    String verb = URLUtils.percentEncode(request.getVerb().name());
    String url = URLUtils.percentEncode(request.getSanitizedUrl());
    String params = getSortedAndEncodedParams(request);
    return String.format(AMPERSAND_SEPARATED_STRING, verb, url, params);
  }

  private String getSortedAndEncodedParams(OAuthRequest request)
  {
    Map<String, List<String>> params = new HashMap<String, List<String>>();
    params.putAll(request.getQueryStringParams());
    params.putAll(request.getBodyParams());
    params.putAll(MapUtils.toListMap(request.getOauthParameters()));
    params = MapUtils.sort(params);
    return URLUtils.percentEncode(URLUtils.formURLEncodeMap(params));
  }

  private void checkPreconditions(OAuthRequest request)
  {
    Preconditions.checkNotNull(request, "Cannot extract base string from null object");

    if (request.getOauthParameters() == null || request.getOauthParameters().size() <= 0)
    {
      throw new OAuthParametersMissingException(request);
    }
  }
}
