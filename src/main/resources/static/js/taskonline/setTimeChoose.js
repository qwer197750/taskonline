function setTimeChoose() {
    $(function () {
        $.fn.datetimepicker.defaults = {
            //默认语言
            language: 'zh-CN',
            //默认选择格式
            format: "yyyy-mm-dd hh:ii",
            autoclose: true,
            todayBtn: true,
            //选择板所在输入框位置
            pickerPosition: "bottom-center"
        };
        $("#timeChoose").datetimepicker();
    });
}