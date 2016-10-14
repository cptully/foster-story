var form_clean;

// serialize clean form
$(function() {
    form_clean = $("form").serialize();
});

// compare clean and dirty form before leaving
window.onbeforeunload = function (e) {
    var form_dirty = $("form").serialize();
    if(form_clean != form_dirty) {
        return 'There is unsaved form data.';
    }
};