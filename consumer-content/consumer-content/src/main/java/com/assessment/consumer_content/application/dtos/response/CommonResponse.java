package com.assessment.consumer_content.application.dtos.response;

import org.springframework.http.HttpStatus;

public class CommonResponse {
    public static Envelope makeResponse(Object data, String message, boolean status) {
        Envelope envelope = new Envelope();

        if (data != null)
        {
            if (data instanceof Exception)
            {
                ErrorMessageResponse response = new ErrorMessageResponse();
                response.setMainErrorMsg(((Exception) data).getStackTrace().toString());
                response.setPublicErrorMsg(((Exception) data).getMessage());
                response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

                envelope.setMessage(message);
                envelope.setStatus(status);
                envelope.setPayload(response);
            }
            else
            {
                envelope.setMessage(message);
                envelope.setStatus(status);
                envelope.setPayload(data);
            }
        }
        else
        {
            envelope.setMessage(message);
            envelope.setStatus(status);
            envelope.setPayload(data);
        }
        return envelope;

    }
}
