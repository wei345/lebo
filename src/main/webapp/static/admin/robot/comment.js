/**
 * @param date
 * @returns string 格式化日期 e.g. "Mon Jan 06 12:35:13 +0800 2014"
 */
function formatDate(date) {

    var weekdaysShort = "Sun_Mon_Tue_Wed_Thu_Fri_Sat".split("_");
    var monthsShort = "Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec".split("_");

    return weekdaysShort[date.getDay()] + " "
        + monthsShort[date.getMonth()] + " "
        + twoNum(date.getDate()) + " "
        + twoNum(date.getHours()) + ":"
        + twoNum(date.getMinutes()) + ":"
        + twoNum(date.getSeconds()) + " "
        + zzzz(date) + " "
        + date.getFullYear();
}

function twoNum(n) {
    return n < 10 ? ("0" + n) : n;
}

function zzzz(date) {
    var minutes = date.getTimezoneOffset();
    var tz;

    if (minutes > 0) {
        tz = "-";
    } else {
        tz = "+";
        minutes = minutes * -1;
    }

    tz += twoNum(Math.floor(minutes / 60));

    tz += twoNum(minutes % 60);

    return tz;
}

function multiSelectAdd(source, target) {
    $(source).find('option:selected').appendTo(target);
}

function multiSelectAddAll(source, target) {
    $(source).find('option').appendTo(target);
}

function preview() {
    var submitBtn = $('#submitBtn').hide();

    var robots = $('#robots-chosen option');
    var sayings = $('#sayings-chosen option');
    var minutes = $('#minutes').val() || 5;
    var mills = minutes * 60 * 1000;
    var startTime = new Date();
    var sayingsIndex = 0;
    var table = $('#previewTable');

    table.empty();

    if (robots.length == 0) {
        table.append('<tr class="error"><td>请选择机器人</td></tr>');
        return;
    }

    if (sayings.length == 0) {
        table.append('<tr class="error"><td>请选择语句</td></tr>');
        return;
    }

    robots.each(function (index, item) {
        var tr = $('<tr><td></td><td></td><td></td></tr>');

        var time = new Date(startTime.getTime() + Math.floor(((index + 1) / robots.length) * mills));
        var robot = $(item);
        var saying = sayings.eq(sayingsIndex++).html();
        if (sayingsIndex == sayings.length) {
            sayingsIndex = 0;
        }

        tr.find('td:first').append('<input value="' + formatDate(time) + '" type="text" class="time input-large"/>');
        tr.find('td:eq(1)').append('<span userid="' + robot.val() + '" class="user">' + robot.html() + '</span>');
        tr.find('td:eq(2)').append('说: <input value="' + saying + '" type="text" class="text input-xxlarge"/>');

        table.append(tr);
    });

    if (table.find('tr').length > 0) {
        submitBtn.show();
    }
}

function submit() {
    var form = $('#inputForm');

    var comments = [];
    $('#previewTable tr').each(function (index, item) {
        comments.push({
            time: $(item).find('.time').val(),
            userId: $(item).find('.user').attr('userid'),
            text: $(item).find('.text').val()
        });
    });

    form[0].comments.value = JSON.stringify(comments);
    form.submit();
}

function addRandomButton(buttonGroup, total, createBtn) {
    var added = {};

    jQuery.each([0.3, 0.5, 0.8], function (index, item) {
        var num = Math.floor(total * item);
        if (num > 0 && !added[num]) {
            $(buttonGroup)
                .find('.dropdown-menu')
                .append(createBtn(num));
            added[num] = true;
        }
    });
}

function addRandomRobot(num) {
    var chosen = [];
    var unchosen = $('#robots-unchosen option');

    while (unchosen.length > 0 && chosen.length < num) {
        chosen.push(unchosen.splice(Math.floor(Math.random() * unchosen.length), 1));
    }

    $('#robots-chosen').append(chosen);
    updateRobotCount();
}

function addRandomSayings(num) {
    var chosen = [];
    var unchosen = $('#sayings-unchosen option');

    while (unchosen.length > 0 && chosen.length < num) {
        chosen.push(unchosen.splice(Math.floor(Math.random() * unchosen.length), 1));
    }

    $('#sayings-chosen').append(chosen);
    updateSayingsCount();
}

function addGroupRobot(group) {
    $('#robots-unchosen option[group*="' + group + ',"]').appendTo('#robots-chosen');
    updateRobotCount();
}

function addTagSayings(tag) {
    $('#sayings-unchosen option[tag*="' + tag + ',"]').appendTo('#sayings-chosen');
    updateSayingsCount();
}

function updateRobotCount() {
    $('#robots-unchosen-count').html($('#robots-unchosen option').length);
    $('#robots-chosen-count').html($('#robots-chosen option').length);
}

function updateSayingsCount() {
    $('#sayings-unchosen-count').html($('#sayings-unchosen option').length);
    $('#sayings-chosen-count').html($('#sayings-chosen option').length);
}