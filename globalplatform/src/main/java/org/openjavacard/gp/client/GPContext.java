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

package org.openjavacard.gp.client;

import org.openjavacard.generic.GenericContext;
import org.openjavacard.gp.keys.GPKeySet;
import org.openjavacard.iso.AID;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

public class GPContext extends GenericContext {

    private boolean mKeyLoggingEnabled = false;

    public GPContext() {
    }

    /** @return true if key logging has been enabled */
    public boolean isKeyLoggingEnabled() {
        return mKeyLoggingEnabled;
    }

    /**
     * Enable logging of keys
     */
    public void enableKeyLogging() {
        LOG.warn("key logging enabled");
        mKeyLoggingEnabled = true;
    }

    public GPCard findSingleGPCard(String prefix) {
        return findSingleGPCard(prefix, null, GPKeySet.GLOBALPLATFORM);
    }

    public GPCard findSingleGPCard(String prefix, AID sd, GPKeySet keys) {
        LOG.debug("findSingleCard()");
        GPCard card;
        CardTerminal terminal = findSingleTerminal(prefix);
        try {
            // check card presence
            if (!terminal.isCardPresent()) {
                throw new Error("No card present in terminal");
            }
            // create GP client
            card = new GPCard(this, terminal);
            // tell the client if we know the SD AID
            if(sd != null) {
                card.setISD(sd);
            }
            if(keys != null) {
                card.setKeys(keys);
            }
            // detect GP applet
            //boolean detected = card.detect();
            //if (!detected) {
            //    throw new Error("Could not find a GlobalPlatform applet on the card");
            //}
        } catch (CardException e) {
            throw new Error("Error detecting card", e);
        }
        return card;
    }

}
