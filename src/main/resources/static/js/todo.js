$(function() {
    $('#tbl_todo').DataTable({
        ordering : false,
        responsive : true,
        stateSave : true,
        dom : '<t><p>',
        pageLength : 5,
        language : {
            zeroRecords : '<div align="center">No To-do Data</div>',
            emptyTable : '<div align="center">No To-do Data</div>',
            paginate : {'next' : '〉', 'previous' : '〈'}
        },
        columnDefs : [
            {
                targets: [0, 1, 2, 3, 4, 5],
                className: 'dt-body-center'
            }
        ]
    });

    // 추가
    $('#btn_add').on('click', function () {
        let addRef = $('#add_ref').val();
        let rExp = /^[0-9,]*$/;

        if ($.trim($('#add_content').val()) === '') {
            alert('내용을 입력해주세요.');
            return;
        }

        if (!rExp.test(addRef)) {
            alert('숫자와 콤마만 입력해주세요.');
            return;
        }

        fnAjax("POST", '/', { content : $('#add_content').val(), ref : addRef });
    });

    // 수정
    $('#tbl_todo').on('click', '.btn_edit', function () {
        let upd_content, upd_ref, old_content, old_ref;
        let mode = $('#tbl_todo').data('mode');
        let td = $(this).closest('tr').find('td');
        let content = td.eq(1);
        let refNo = td.eq(2);

        // 수정
        if (mode === 'default') {
            $('#tbl_todo').data('mode', 'update');
            content.html('<input type="text" id="upd_content" value="' + content.text() + '">');
            refNo.html('<input type="text" id="upd_ref" value="' + refNo.text() + '">');
        }

        // 수정 완료
        else {
            $('#tbl_todo').data('mode', 'default');
            old_content = content.data('content');
            old_ref = refNo.data('ref') === undefined ? '' : refNo.data('ref');
            upd_content = content.find('input').val();
            upd_ref = refNo.find('input').val();

            content.html(upd_content);
            refNo.html(upd_ref);

            // 내용에 변화가 없으면 수정 안함
            if (upd_content != old_content
                    || upd_ref != old_ref) {
                fnAjax('PUT', '/' + $(this).data('no'), { content: upd_content, ref: upd_ref });
            }
        }
    });


    // 검색
    $('#todo_search').on('click', function () {
        fnSearch();
    });


    // 검색
    $("#todo_item").keydown(function(key) {
        if (key.keyCode === 13) {
            fnSearch();
        }
    });

    $('#todo_item').focus();
});

function fnSearch() {
    $(location).attr('href', '/todo?todo_type=' + $('#todo_type').val() +'&todo_item=' + $('#todo_item').val());
}

function fnDelete(no) {
    if (!confirm('삭제 하시겠습니까?')) return false;
    fnAjax('DELETE', '/' + no, '');
}

function fnChgStatus(no) {
    let isFinish = 'N';

    if ($('#chk_' + no).is(':checked')) {
        isFinish = 'Y'
    }

    fnAjax('PUT', '/status/' + no, {'isFinish' : isFinish});
}

function fnAjax(method, url, data) {
    $.ajax({
        type: method,
        url: '/todo' + url,
        data : JSON.stringify(data),
        contentType:'application/json; charset=utf-8',
        success: function (data) {
            if (method === 'PUT' && data === 0) {
                if (url.toString().indexOf('status') !== -1) {
                    alert('완료되지 않은 참조가 있어 완료할 수 없습니다.');
                }
                else {
                    alert('수정에 실패하였습니다.');
                }
            }

            if (method === 'DELETE' && data === 0) {
                alert('삭제에 실패하였습니다.');
            }

            $(location).attr('href', '/todo');
        },
        error: function (xhr, status, error) {
            alert('Ajax Error');
            $(location).attr('href', '/todo');
        }
    });
}
