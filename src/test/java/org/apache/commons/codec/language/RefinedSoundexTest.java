/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

/**
 * Tests RefinedSoundex.
 */
class RefinedSoundexTest extends AbstractStringEncoderTest<RefinedSoundex> {

    @Override
    protected RefinedSoundex createStringEncoder() {
        return new RefinedSoundex();
    }

    @Test
    void testDifference() throws EncoderException {
        // Edge cases
        assertEquals(0, getStringEncoder().difference(null, null));
        assertEquals(0, getStringEncoder().difference("", ""));
        assertEquals(0, getStringEncoder().difference(" ", " "));
        // Normal cases
        assertEquals(6, getStringEncoder().difference("Smith", "Smythe"));
        assertEquals(3, getStringEncoder().difference("Ann", "Andrew"));
        assertEquals(1, getStringEncoder().difference("Margaret", "Andrew"));
        assertEquals(1, getStringEncoder().difference("Janet", "Margaret"));
        // Examples from
        // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_de-dz_8co5.asp
        assertEquals(5, getStringEncoder().difference("Green", "Greene"));
        assertEquals(1, getStringEncoder().difference("Blotchet-Halls", "Greene"));
        // Examples from
        // https://msdn.microsoft.com/library/default.asp?url=/library/en-us/tsqlref/ts_setu-sus_3o6w.asp
        assertEquals(6, getStringEncoder().difference("Smith", "Smythe"));
        assertEquals(8, getStringEncoder().difference("Smithers", "Smythers"));
        assertEquals(5, getStringEncoder().difference("Anothers", "Brothers"));
    }

    @Test
    void testEncode() {
        assertEquals("T6036084", getStringEncoder().encode("testing"));
        assertEquals("T6036084", getStringEncoder().encode("TESTING"));
        assertEquals("T60", getStringEncoder().encode("The"));
        assertEquals("Q503", getStringEncoder().encode("quick"));
        assertEquals("B1908", getStringEncoder().encode("brown"));
        assertEquals("F205", getStringEncoder().encode("fox"));
        assertEquals("J408106", getStringEncoder().encode("jumped"));
        assertEquals("O0209", getStringEncoder().encode("over"));
        assertEquals("T60", getStringEncoder().encode("the"));
        assertEquals("L7050", getStringEncoder().encode("lazy"));
        assertEquals("D6043", getStringEncoder().encode("dogs"));

        // Testing CODEC-56
        assertEquals("D6043", RefinedSoundex.US_ENGLISH.encode("dogs"));
    }

    @Test
    void testGetMappingCodeNonLetter() {
        final char code = getStringEncoder().getMappingCode('#');
        assertEquals(0, code, "Code does not equals zero");
    }

    @Test
    void testInvalidSoundexCharacter() {
        final char[] invalid = new char[256];
        for (int i = 0; i < invalid.length; i++) {
            invalid[i] = (char) i;
        }

        assertEquals(new RefinedSoundex().encode(new String(invalid)), "A0136024043780159360205050136024043780159360205053");
    }

    @Test
    void testNewInstance() {
        assertEquals("D6043", new RefinedSoundex().soundex("dogs"));
    }

    @Test
    void testNewInstance2() {
        assertEquals("D6043", new RefinedSoundex(RefinedSoundex.US_ENGLISH_MAPPING_STRING.toCharArray()).soundex("dogs"));
    }

    @Test
    void testNewInstance3() {
        assertEquals("D6043", new RefinedSoundex(RefinedSoundex.US_ENGLISH_MAPPING_STRING).soundex("dogs"));
    }
}
