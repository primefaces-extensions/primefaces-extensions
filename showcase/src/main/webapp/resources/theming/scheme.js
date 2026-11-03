/*
 * data-dark on <html>: the effective color scheme for the showcase layout (code highlighting, editors, charts, maps).
 * Included by the templates with data-p-no-move, after the theme.js of PrimeFaces that applies the stored color scheme,
 * so it is set before the first paint; updated on changes of the theme configurator (p-theme-change), of the color-scheme
 * of <html> (e.g. pe:lightSwitch) and of the system setting.
 *
 * Also the preset links of the landing page (data-preset-link="<preset>"): like the presets of the configurator, the
 * cookie primefaces.theme and a reload.
 */
(function () {
    var root = document.documentElement;
    var media = window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)');

    function sync() {
        var scheme = root.style.colorScheme;
        var dark = scheme ? scheme === 'dark' : !!(media && media.matches);
        var value = dark ? 'true' : 'false';
        if (root.getAttribute('data-dark') !== value) {
            root.setAttribute('data-dark', value);
        }
    }

    sync();
    if (media) {
        media.addEventListener('change', sync);
    }
    document.addEventListener('p-theme-change', sync);
    if (window.MutationObserver) {
        new MutationObserver(sync).observe(root, { attributes: true, attributeFilter: ['style'] });
    }

    document.addEventListener('click', function (e) {
        var link = e.target instanceof Element ? e.target.closest('[data-preset-link]') : null;
        if (!link || !window.PrimeFaces) {
            return;
        }
        e.preventDefault();
        PrimeFaces.setCookie('primefaces.theme', link.getAttribute('data-preset-link'),
            { path: PrimeFaces.settings.contextPath || '/', expires: 365 });
        window.location.reload();
    });
})();
