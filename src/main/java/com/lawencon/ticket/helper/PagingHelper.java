package com.lawencon.ticket.helper;

import com.lawencon.ticket.model.request.PagingRequest;
import com.lawencon.ticket.model.response.PagingResponse;

public class PagingHelper {
    public static PagingResponse toPaging(PagingRequest request, Long totalItem) {
        long totalPage = totalItem / request.getPageSize();
        if (totalItem % request.getPageSize() != 0) {
            totalPage++;
        }
        return toPaging(request, Math.toIntExact(totalPage), totalItem);
    }

    public static PagingResponse toPaging(PagingRequest request, Integer totalPage,
            Long totalItem) {
        return PagingResponse.builder().page(request.getPage() + 1).pageSize(request.getPageSize())
                .totalItem(totalItem).totalPage(totalPage).sortBy(request.getSortBy()).build();
    }
}

