/**
 * Copyright (C) 2011 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.cloudhopper.smpp.type;

import com.cloudhopper.commons.util.HexUtil;
import com.cloudhopper.smpp.pdu.PduResponse;

/**
 * 
 * @author joelauer
 */
public class UnexpectedPduResponseException extends UnrecoverablePduException {
    static final long serialVersionUID = 1L;

    private final PduResponse responsePdu;

    public UnexpectedPduResponseException(PduResponse responsePdu) {
        super(buildErrorMessage(responsePdu));
        this.responsePdu = responsePdu;
    }

    public PduResponse getResponsePdu() {
        return this.responsePdu;
    }

    static public String buildErrorMessage(PduResponse responsePdu) {
        return "Unexpected response PDU [" + responsePdu.getClass().getName() + "] [error: 0x" + HexUtil.toHexString(responsePdu.getCommandStatus()) + " \"" + responsePdu.getResultMessage() + "\"]";
    }
}