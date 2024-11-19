package com.lawencon.ticket.model.response;

import java.util.List;
import com.lawencon.ticket.model.request.SortBy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagingResponse {
    private Integer page;
    private Integer pageSize;
    private Integer totalPage;
    private Long totalItem;
    private List<SortBy> sortBy;
}
