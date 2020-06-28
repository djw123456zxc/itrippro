package dao.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;

import java.io.IOException;
import java.util.List;

public class SolrDao<T> {

    private HttpSolrClient httpSolrClient = null;

    private QueryResponse response = null;

    public SolrDao(String url) {
        //创建httpSolrClient
        httpSolrClient = new HttpSolrClient(url);
        //配置解析器
        httpSolrClient.setParser(new XMLResponseParser());
        httpSolrClient.setConnectionTimeout(500);
    }

    public List<T> queryList(SolrQuery query,Class clazz){
        List<T> list = null;
        try {
            response = httpSolrClient.query(query);
            list = response.getBeans(clazz);
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return list;
        }
    }

}

