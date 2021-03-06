/*
 * openjavacard-tools: Development tools for JavaCard
 * Copyright (C) 2018 Ingo Albrecht <copyright@promovicz.org>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */

package org.openjavacard.jackson.iso;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.openjavacard.emv.CPLC;

import java.io.IOException;

public class CPLCSerializer extends StdSerializer<CPLC> {

    public CPLCSerializer() {
        super(CPLC.class);
    }

    @Override
    public void serialize(CPLC value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        gen.writeStartObject();
        for(CPLC.Field field: CPLC.Field.values()) {
            gen.writeStringField(field.name(), value.getFieldHex(field));
        }
        gen.writeEndObject();
    }
}
