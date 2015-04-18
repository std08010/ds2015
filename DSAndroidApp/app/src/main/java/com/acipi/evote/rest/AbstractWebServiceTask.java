package com.acipi.evote.rest;

import android.os.AsyncTask;
import android.util.Log;

import com.acipi.evote.rest.dto.AbstractInDTO;
import com.acipi.evote.rest.dto.AbstractOutDTO;
import com.acipi.evote.rest.dto.ErrorDTO;
import com.acipi.evote.utils.EasySSLSocketFactory;
import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Altin Cipi on 1/30/2015.
 */
public abstract class AbstractWebServiceTask<I extends AbstractInDTO, O extends AbstractOutDTO> extends AsyncTask<String, Integer, AbstractOutDTO>
{
    public static final int POST = 1;
    public static final int GET = 2;

    // connection timeout, in milliseconds (waiting to connect)
    private static final int CONN_TIMEOUT = 30000;

    // socket timeout, in milliseconds (waiting for data)
    private static final int SOCKET_TIMEOUT = 60000;

    private static HttpClient httpClient;

    private int taskType = GET;

    private I inDTO;

    private Class<O> outDTOClass;

    private Gson gson = new Gson();

    public AbstractWebServiceTask(int taskType, I inDTO, Class<O> outDTOClass)
    {
        this.taskType = taskType;
        this.inDTO = inDTO;
        this.outDTOClass = outDTOClass;
    }

    @Override
    protected void onPreExecute()
    {
        onInitActions();
    }

    @Override
    protected void onPostExecute(AbstractOutDTO outDTO)
    {
        if (outDTO instanceof ErrorDTO)
        {
            handleError((ErrorDTO) outDTO);
        }
        else
        {
            handleResponse(outDTO);
        }
    }

    @Override
    protected AbstractOutDTO doInBackground(String... urls)
    {
        String url = urls[0];

        HttpResponse response = getResponse(url);

        if (response != null)
        {
            try
            {
                final int statusCode = response.getStatusLine().getStatusCode();

                Reader reader = new InputStreamReader(response.getEntity().getContent());

                if (statusCode != HttpStatus.SC_OK)
                {
                    Log.w(getClass().getSimpleName(), "Error " + statusCode + " " + response.getStatusLine().getReasonPhrase() + " for URL " + url);
                    return gson.fromJson(reader, ErrorDTO.class);
                }

                return gson.fromJson(reader, outDTOClass);
            }
            catch (Exception e)
            {
                Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
            }
        }

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage("Server did not respond. Please try again later.");
        errorDTO.setStatusCode(HttpStatus.SC_NOT_FOUND);

        return errorDTO;
    }

    private synchronized static HttpClient getHttpClient()
    {
        if (httpClient != null)
        {
            return httpClient;
        }

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONN_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, SOCKET_TIMEOUT);
        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);

        //Thread safe in case various AsyncTasks try to access it concurrently
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
//        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 8443)); //trust only valid
        schemeRegistry.register(new Scheme("https", new EasySSLSocketFactory(), 8443)); //trust everyone
        ClientConnectionManager cm = new ThreadSafeClientConnManager(httpParameters, schemeRegistry);

        httpClient = new DefaultHttpClient(cm, httpParameters);

        return httpClient;
    }

    private HttpResponse getResponse(String url)
    {
        HttpResponse response = null;

        try
        {
            switch (taskType)
            {
                case POST:
                    HttpPost httppost = new HttpPost(url);

                    httppost.setEntity(new StringEntity(gson.toJson(inDTO)));
                    httppost.setHeader("Accept", "application/json");
                    httppost.setHeader("Content-type", "application/json");

                    response = getHttpClient().execute(httppost);
                    break;
                case GET:
                    HttpGet httpget = new HttpGet(url);

                    response = getHttpClient().execute(httpget);
                    break;
            }
        }
        catch (Exception e)
        {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
        }

        return response;
    }

    public abstract void onInitActions();

    public abstract void handleResponse(AbstractOutDTO outDTO);

    public abstract void handleError(ErrorDTO errorDTO);
}
