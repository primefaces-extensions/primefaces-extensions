// Monaco themes are chosen by name. This keeps the editors with the default theme "vs" in line with the color scheme of the
// page (data-dark on <html>, set by the theme configurator): "vs-dark" in dark mode, "vs" in light mode. An editor with another
// theme (e.g. selected in a menu) keeps it. Use it as the oninitialized callback: oninitialized="monacoFollowScheme.call(this)"
function monacoFollowScheme() {
    var dark = document.documentElement.getAttribute('data-dark') === 'true';
    var options = this.cfg.editorOptions;
    if ((typeof options === 'string' ? JSON.parse(options) : options).theme === 'vs') {
        this.invokeMonaco('updateOptions', {theme: dark ? 'vs-dark' : 'vs'});
    }
}

// switching the scheme later: update the editors that are ready
new MutationObserver(function () {
    Object.values(PrimeFaces.widgets).forEach(function (widget) {
        if (widget instanceof PrimeFaces.widget.ExtMonacoBaseEditor && widget.isReady()) {
            monacoFollowScheme.call(widget);
        }
    });
}).observe(document.documentElement, {attributes: true, attributeFilter: ['data-dark']});
