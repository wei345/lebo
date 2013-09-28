/**
 * User: liuwei
 * Date: 13-6-2
 * Time: PM5:52
 */

if(jQuery && jQuery.validator){
    jQuery.validator.addMethod(
        "regex",
        function(value, element, regexp) {
            var re = new RegExp(regexp);
            return this.optional(element) || re.test(value);
        },
        "Please check your input."
    );
}

