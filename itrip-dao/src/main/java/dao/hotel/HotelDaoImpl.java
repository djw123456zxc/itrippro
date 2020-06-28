package dao.hotel;

import dao.solr.SolrDao;
import org.apache.solr.client.solrj.SolrQuery;
import vo.hotel.HotelVo;
import vo.hotel.SearchHotelVO;

import java.util.List;

public class HotelDaoImpl implements HotelDao{
    private static String url = "http://localhost:8080/solr/hotel";
    private SolrDao<HotelVo> hotelBaseDao = new SolrDao<HotelVo>(url);

    public List<HotelVo> queryHotelList(SearchHotelVO searchHotelVO){
        SolrQuery query = new SolrQuery("*:*");
        StringBuilder params = new StringBuilder();
        try {
            if (searchHotelVO.getDestination() != null && !"".equals(searchHotelVO.getDestination())) {
                searchHotelVO.setDestination(new String(searchHotelVO.getDestination().getBytes("iso-8859-1"), "utf-8"));
                params.append(" AND destination:"+searchHotelVO.getDestination());
            }
            if(searchHotelVO.getHotelLevel()!=null && searchHotelVO.getHotelLevel()>0){
                params.append(" AND hotelLevel:"+searchHotelVO.getHotelLevel());
            }
            if(searchHotelVO.getKeywords()!=null && !"".equals(searchHotelVO.getKeywords())){
                searchHotelVO.setKeywords(new String(searchHotelVO.getKeywords().getBytes("iso-8859-1"), "utf-8"));
                params.append(" AND keyword:"+searchHotelVO.getKeywords());
            }
            if(searchHotelVO.getTradeAreaIds()!=null && !"".equals(searchHotelVO.getTradeAreaIds())){
                params.append(" AND tradingAreaIds:,*"+searchHotelVO.getTradeAreaIds()+"*");
            }
            if(searchHotelVO.getMinPrice()!=null && searchHotelVO.getMinPrice()>0){
                params.append(" AND minPrice:["+searchHotelVO.getMinPrice()+" TO *]");
            }
            if(searchHotelVO.getMaxPrice()!=null && searchHotelVO.getMaxPrice()>0){
                params.append(" AND maxPrice:[* TO "+searchHotelVO.getMaxPrice()+"]");
            }
            if(searchHotelVO.getFeatureIds()!=null && !"".equals(searchHotelVO.getFeatureIds())){
                params.append(" AND featureIds:,*"+searchHotelVO.getFeatureIds()+"*");
            }
            if(searchHotelVO.getAscSort()!=null && !"".equals(searchHotelVO.getAscSort())){
                query.setSort(searchHotelVO.getAscSort(), SolrQuery.ORDER.asc);
            }
            if(searchHotelVO.getDescSort()!=null && !"".equals(searchHotelVO.getDescSort())){
                query.setSort(searchHotelVO.getDescSort(), SolrQuery.ORDER.desc);
            }
            if(searchHotelVO.getPageSize()!=null && searchHotelVO.getPageNo()!=null){
                query.setStart((searchHotelVO.getPageNo()-1)*searchHotelVO.getPageSize());
                query.setRows(searchHotelVO.getPageSize());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        String filterquery=params.toString();
        if(filterquery!=null&&!"".equals(filterquery)){
            filterquery=filterquery.substring(4);
        }
        System.out.println("条件为"+filterquery+" 当前页码为"+searchHotelVO.getPageNo()+" 每页条数为"+searchHotelVO.getPageSize());
        query.setFilterQueries(filterquery);
        List<HotelVo> list = hotelBaseDao.queryList(query,HotelVo.class);
        return list;
    }
}
