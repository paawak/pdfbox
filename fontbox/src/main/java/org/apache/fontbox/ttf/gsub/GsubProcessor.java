/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.fontbox.ttf.gsub;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class splits an array of GlyphIds with a prospective match.
 *
 */
public class GsubProcessor
{
    private static final String GLYPH_ID_SEPARATOR = "_";

    private final CompoundCharacterTokenizer compoundCharacterTokenizer;

    public GsubProcessor(Set<List<Integer>> matchers)
    {
        compoundCharacterTokenizer = new CompoundCharacterTokenizer(getMatchersAsStrings(matchers));
    }

    public List<List<Integer>> tokenize(List<Integer> glyphIds)
    {
        String originalGlyphsAsText = convertGlyphIdsToString(glyphIds);
        List<String> tokens = compoundCharacterTokenizer.tokenize(originalGlyphsAsText);

        List<List<Integer>> modifiedGlyphs = new ArrayList<>();

        for (String token : tokens)
        {
            modifiedGlyphs.add(convertGlyphIdsToList(token));
        }

        return modifiedGlyphs;
    }

    private Set<String> getMatchersAsStrings(Set<List<Integer>> matchers)
    {
        Set<String> stringMatchers = new HashSet<>(matchers.size());
        for (List<Integer> glyphIds : matchers)
        {
            stringMatchers.add(convertGlyphIdsToString(glyphIds));
        }
        return stringMatchers;
    }

    private String convertGlyphIdsToString(List<Integer> glyphIds)
    {
        StringBuilder sb = new StringBuilder(20);
        for (Integer glyphId : glyphIds)
        {
            sb.append(glyphId).append(GLYPH_ID_SEPARATOR);
        }
        sb.setLength(sb.length() - GLYPH_ID_SEPARATOR.length());
        return sb.toString();
    }

    private List<Integer> convertGlyphIdsToList(String glyphIdsAsString)
    {
        List<Integer> gsubProcessedGlyphsIds = new ArrayList<>();

        for (String glyphId : glyphIdsAsString.split(GLYPH_ID_SEPARATOR))
        {
            if (glyphId.trim().length() == 0)
            {
                continue;
            }
            gsubProcessedGlyphsIds.add(Integer.valueOf(glyphId));
        }

        return gsubProcessedGlyphsIds;
    }

}
