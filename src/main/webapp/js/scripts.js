$(document).ready(load(true))

function load(variable) {
    $('#table_body tr').remove();
    $.ajax({
        url: getContextPath() + "/post",
        method: 'POST',
        data: JSON.stringify({"action": "getAll"}),
        dataType: 'json'
    }).done(function (data) {
        $(data).each(function (index, el) {
            if (variable || !(el.done)) {
                $('#table_body').append(
                    '<tr><td>' + el.id + '</td><td>' + el.description + '</td><td>'
                    + new Date(el.created).toLocaleString() + '</td><td>' + ifDone(el.done, el.id) + '</td></tr>'
                );
            }
        })

    })
}


function ifDone(check, num) {
    var checked = "<div class=\"mychekbox\"> " +
        "    <label><input checked id=" + num + " onchange='updateExecution(" + num + ")' type=\"checkbox\"></label>" +
        " </div>";
    var uncheked = "<div class=\"mychekbox\">\n" +
        "    <label><input id=" + num + " onchange='updateExecution(" + num + ")' type=\"checkbox\"></label>\n" +
        "</div>";
    if (check) {
        return checked;
    } else {
        return uncheked;
    }
}

function createTask() {
    var date = Date.now();
    var text = $('#textdesc').val();
    $.ajax({
        url: getContextPath() + "/post",
        method: 'POST',
        contentType: 'json',
        data: JSON.stringify({"action": "save", "description": text, "created": date})
    }).done(function () {
        load(true);
        $('#textdesc').val("");
    }).fail(function () {
        alert("something wrong");
    })
}

function updateExecution(num) {
    $.ajax({
        url: getContextPath() + "/post",
        method: 'POST',
        contentType: 'json',
        data: JSON.stringify({"action": "update", "id": num + ""})
    }).fail(function () {
        var box = $('#' + num);
        if (box.is(':checked')) {
            box.prop('checked', false);
        } else {
            box.prop('checked', true);
        }
    })
}

function showUndone() {
    var temp = $('#mycheck').val();
    if (temp == "0") {
        load(false);
        $('#mycheck').val("1");
    } else {
        load(true);
        $('#mycheck').val("0");
    }
}

function getContextPath() {
    return location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
}