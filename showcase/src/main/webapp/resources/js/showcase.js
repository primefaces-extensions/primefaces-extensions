/**
 * Showcase Library of JS functions.
 */
var Showcase = (function () {
    return {
        /**
         * https://stackoverflow.com/questions/9550760/hide-page-until-everything-is-loaded-advanced/9551066
         */
        displayPage: function () {
            // apply HighlightJS
            var pres = document.querySelectorAll("pre>code");
            for (var i = 0; i < pres.length; i++) {
                hljs.highlightBlock(pres[i]);
            }
            // add HighlightJS-badge (options are optional)
            var options = {   // optional
                contentSelector: "#ArticleBody",

                // CSS class(es) used to render the copy icon.
                copyIconClass: "fas fa-copy",
                // CSS class(es) used to render the done icon.
                checkIconClass: "fas fa-check text-success"
            };
            window.highlightJsBadge(options);

            // configure the layout
            $(document).ready(function () {
                // trim whitespace from code blocks
                $("code[class^='language-']").each(function () {
                    $(this).html($(this).html().trim());
                });

                // remove the z-index so some elements get rendered on top
                $("#layoutCenter").css("z-index", '');

                // show the HTML pane after everything has been asjusted
                // https://stackoverflow.com/questions/9550760/hide-page-until-everything-is-loaded-advanced/9551066
                document.getElementsByTagName("html")[0].style.visibility = "visible";

                $(document).on("click", function (event) {
                    if (window.innerWidth < 991) {
                        var westPane = $("#layoutWest, .ui-layout-pane-west");
                        var eastPane = $("#layoutEast, .ui-layout-pane-east");
                        
                        if (westPane.hasClass("mobile-open") && !westPane.is(event.target) && westPane.has(event.target).length === 0 && !$(event.target).closest(".mobile-menu-btn-west").length) {
                            westPane.removeClass("mobile-open");
                        }
                        
                        if (eastPane.hasClass("mobile-open") && !eastPane.is(event.target) && eastPane.has(event.target).length === 0 && !$(event.target).closest(".mobile-menu-btn-east").length) {
                            eastPane.removeClass("mobile-open");
                        }
                    }
                });
            });
        },

        selectComponentLink: function (link) {
            $("#componentList").find(".ui-state-active").removeClass("ui-state-active");
            if (link) {
                $(link).addClass("ui-state-active");
            }
        },

        selectUseCaseLink: function (link) {
            $("#useCaseList").find(".ui-state-active").removeClass("ui-state-active");
            if (link) {
                $(link).addClass("ui-state-active");
            }
        }
    };
})();
