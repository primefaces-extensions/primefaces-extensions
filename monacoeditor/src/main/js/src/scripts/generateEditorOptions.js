// @ts-check

// This file contains the editor options from the official API docs
// https://microsoft.github.io/monaco-editor/api/interfaces/monaco.editor.IStandaloneEditorConstructionOptions.html
// 
// This is used to create corresponding Java classes for configuring the monaco editor widget

import {
    cleanJavaDescriptors,
    Deprecated,
    Doc,
    T_Array,
    T_Boolean,
    T_BooleanOrEnum,
    T_BooleanOrString,
    T_Class,
    T_CssSize,
    T_Enum,
    T_Map,
    T_Number,
    T_String
} from "../util/java-types.js";

async function main() {
    await cleanJavaDescriptors();

    const EGoToLocationValues = T_Enum("EGoToLocationValues",
        "Defines the behavior of a goto command in the editor.",
        false,

        [Doc("Open an inline window with a peek preview of the location.")],
        "peek",

        [Doc("Open an inline window with a peek preview of the location and offer the option to navigation to the source code location.")],
        "gotoAndPeek",

        [Doc("Navigate directly to the source code location.")],
        "goto"
    );

    const ETheme = T_Enum("ETheme",
        "Color theme for the Monaco Code editor",
        false,

        [Doc("The standard Visual Studio light theme.")],
        "vs",

        [Doc("The standard Visual Studio dark theme.")],
        "vs-dark",

        [Doc("The high-contrast theme for improved accessibility.")],
        "hc-black"
    );

    const EditorFindOptions = T_Class("EditorFindOptions", {
        addExtraSpaceOnTop: T_Boolean(),

        [Doc()]: "Controls if Find in Selection flag is turned on in the editor.",
        autoFindInSelection: T_Enum("EAutoFindInSelection",
            "Controls if Find in Selection flag is turned on in the editor.",
            false,
            "never", "always", "multiline"
        ),

        [Doc()]: "Controls whether the cursor should move to find matches while typing.",
        cursorMoveOnType: T_Boolean(),

        [Doc()]: "Controls whether the search automatically restarts from the beginning (or the end) when no further matches can be found.",
        loop: T_Boolean(),

        [Doc()]: "Controls if we seed search string in the Find Widget with editor selection.",
        seedSearchStringFromSelection: T_Enum("ESeedSearchStringFromSelection",
            "Controls if we seed search string in the Find Widget with editor selection.",
            false,
            "always", "never", "selection"
        ),
    }, "Configuration options for editor find widget");

    const EditorRulerOption = T_Class("EditorRulerOption", {
        [Doc()]: "CSS color of the vertical ruler line.",
        color: T_String(),

        [Doc()]: "Position in characters from the left edge of the vertical ruler line.",
        column: T_Number(),
    }, "How to render vertical lines at the specified columns");

    const EditorGuidesOptions = T_Class("EditorGuidesOptions", {
        [Doc()]: "Enable rendering of bracket pair guides. Defaults to {@code false}.",
        bracketPairs: T_Boolean(),
        [Doc()]: "Enable rendering of vertical bracket pair guides. Defaults to {@code true}.",
        bracketPairsHorizontal: T_Boolean(),
        [Doc()]: "Enable highlighting of the active bracket pair. Defaults to {@code true}.",
        highlightActiveBracketPair: T_Boolean(),
        [Doc()]: "Enable highlighting of the active indent guide. Defaults to {@code true}.",
        highlightActiveIndentation: T_Boolean(),
        [Doc()]: "Enable rendering of indent guides. Defaults to {@code true}.",
        indentation: T_Boolean(),
    }, "Controls the behavior of editor guides.");

    const EditorGotoLocationOptions = T_Class("EditorGotoLocationOptions", {
        alternativeDeclarationCommand: T_String(),
        alternativeDefinitionCommand: T_String(),
        alternativeImplementationCommand: T_String(),
        alternativeReferenceCommand: T_String(),
        alternativeTypeDefinitionCommand: T_String(),
        multiple: EGoToLocationValues,
        multipleDeclarations: EGoToLocationValues,
        multipleDefinitions: EGoToLocationValues,
        multipleImplementations: EGoToLocationValues,
        multipleReferences: EGoToLocationValues,
        multipleTypeDefinitions: EGoToLocationValues,
    }, "Configuration options for go to location");

    const EditorHoverOptions = T_Class("EditorHoverOptions", {
        [Doc()]: "Should the hover be shown above the line if possible? Defaults to {@code false}.",
        above: T_Boolean(),

        [Doc()]: "Delay for showing the hover. Defaults to 300.",
        delay: T_Number(),

        [Doc()]: "Enable the hover. Defaults to {@code true}.",
        enabled: T_Boolean(),

        [Doc()]: "Is the hover sticky such that it can be clicked and its contents selected? Defaults to {@code true}.",
        sticky: T_Boolean(),
    }, "Configuration options for editor hover");

    const EditorInlayHintOptions = T_Class("EditorInlayHintOptions", {
        [Doc()]: "Enable the inline hints. Defaults to {@code true}.",
        enabled: T_Boolean(),

        [Doc()]: "Font family of inline hints. Defaults to editor font family.",
        fontFamily: T_String(),

        [Doc()]: "Font size of inline hints. Default to 90% of the editor font size.",
        fontSize: T_Number(),

        [Doc()]: "Enables the padding around the inlay hint. Defaults to {@code false}.",
        padding: T_Boolean(),
    }, "Control the behavior and rendering of the inline hints.");

    const EditorInlineSuggestOptions = T_Class("EditorInlineSuggestOptions", {
        [Doc()]: "Enable or disable the rendering of automatic inline completions.",
        enabled: T_Boolean(),

        [Doc()]: "Configures the mode. Use prefix to only show ghost text if the text to replace is a prefix of the suggestion text. Use {@link subword} to only show ghost text if the replace text is a sub word of the suggestion text. Use {@link subwordSmart} to only show ghost text if the replace text is a sub word of the suggestion text, but the sub word must start after the cursor position. Defaults to {@code prefix}.",
        mode: T_Enum("EInlineSuggestMode",
            "Configures the mode. Use prefix to only show ghost text if the text to replace is a prefix of the suggestion text. Use {@link subword} to only show ghost text if the replace text is a sub word of the suggestion text. Use {@link subwordSmart} to only show ghost text if the replace text is a sub word of the suggestion text, but the sub word must start after the cursor position. Defaults to {@code prefix}.",
            false,
            "prefix", "subword", "subwordSmart"
        ),

        [Doc()]: "Configures whether to show the inline suggest toolbar.",
        showToolbar: T_Enum("EShowToolbarMode",
            "Configures whether to show the inline suggest toolbar.",
            false,
            "always", "onHover"
        ),
    }, "Control the behavior and rendering of the inline completions.");

    const EditorLightbulbOptions = T_Class("EditorLightbulbOptions", {
        [Doc()]: "Enable the lightbulb code action. Defaults to {@code true}.",
        enabled: T_Boolean(),
    }, "Configuration options for editor lightbulb");

    const EditorMinimapOptions = T_Class("EditorMinimapOptions", {
        [Doc()]: "Control the rendering of minimap.",
        autohide: T_Boolean(),

        [Doc()]: "Enable the rendering of the minimap. Defaults to {@code true}.",
        enabled: T_Boolean(),

        [Doc()]: "Limit the width of the minimap to render at most a certain number of columns. Defaults to {@code 120}.",
        maxColumn: T_Number(),

        [Doc()]: "Render the actual text on a line (as opposed to color blocks). Defaults to {@code true}.",
        renderCharacters: T_Boolean(),

        [Doc()]: "Relative size of the font in the minimap. Defaults to {@code 1}.",
        scale: T_Number(),

        [Doc()]: "Control the rendering of the minimap slider. Defaults to {@code mouseover}.",
        showSlider: T_Enum("EMinimapShowSlider",
            "Control the rendering of the minimap slider. Defaults to {@code mouseover}.",
            false,
            "always", "mouseover"
        ),

        [Doc()]: "Control the side of the minimap in editor. Defaults to {@code right}.",
        side: T_Enum("EMinimapSide",
            "Control the side of the minimap in editor. Defaults to {@code right}.",
            false,
            "right", "left"
        ),

        [Doc()]: "Control the minimap rendering mode. Defaults to actual.",
        size: T_Enum("EMinimapSize",
            "Control the minimap rendering mode. Defaults to actual.",
            false,
            "proportional", "fill", "fit"
        ),
    }, "Configuration options for editor minimap");

    const EditorPaddingOptions = T_Class("EditorPaddingOptions", {
        [Doc()]: "Spacing between bottom edge of editor and last line.",
        bottom: T_Number(),

        [Doc()]: "Spacing between top edge of editor and first line.",
        top: T_Number(),
    }, "Configuration options for editor padding.");

    const EditorParameterHints = T_Class("EditorParameterHints", {
        [Doc()]: "Enable cycling of parameter hints. Defaults to {@code false}.",
        cycle: T_Boolean(),

        [Doc()]: "Enable parameter hints. Defaults to {@code true}.",
        enabled: T_Boolean(),
    }, "Configuration options for parameter hints");

    const EditorSmartSelectOptions = T_Class("EditorSmartSelectOptions", {
        selectLeadingAndTrailingWhitespace: T_Boolean(),
    }, "Smart select options.");

    const EditorStickyScrollOptions = T_Class("EditorStickyScrollOptions", {
        [Doc()]: "Enable the sticky scroll",
        enabled: T_Boolean(),

        [Doc()]: "Maximum number of sticky lines to show",
        maxLineCount: T_Number(),
    }, "Control the behavior of sticky scroll options");

    const EditorScrollbarOptions = T_Class("EditorScrollbarOptions", {
        [Doc()]: "Always consume mouse wheel events (always call {@code preventDefault()} and {@code stopPropagation()} on the browser events). Defaults to {@code true}.",
        alwaysConsumeMouseWheel: T_Boolean(),

        [Doc()]: "The size of arrows (if displayed). Defaults to {@code 11}.",
        arrowSize: T_Number(),

        [Doc()]: "Listen to mouse wheel events and react to them by scrolling. Defaults to {@code true}.",
        handleMouseWheel: T_Boolean(),

        [Doc()]: "Render horizontal scrollbar. Defaults to {@code auto}.",
        horizontal: T_Enum("EScrollbarHorizontal",
            "Render horizontal scrollbar. Defaults to {@code auto}.",
            false,
            "auto", "visible", "hidden"
        ),

        [Doc()]: "Render arrows at the left and right of the horizontal scrollbar. Defaults to {@code false}.",
        horizontalHasArrows: T_Boolean(),

        [Doc()]: "Height in pixels for the horizontal scrollbar. Defaults to {@code 10} (px).",
        horizontalScrollbarSize: T_Number(),

        [Doc()]: "Height in pixels for the horizontal slider. Defaults to {@code horizontalScrollbarSize}.",
        horizontalSliderSize: T_Number(),

        [Doc()]: "Scroll gutter clicks move by page vs jump to position. Defaults to {@code false}.",
        scrollByPage: T_Boolean(),

        [Doc()]: "Cast horizontal and vertical shadows when the content is scrolled. Defaults to {@code true}.",
        useShadows: T_Boolean(),

        [Doc()]: "Render vertical scrollbar. Defaults to {@code auto}.",
        vertical: T_Enum("EScrollbarVertical",
            "Render vertical scrollbar. Defaults to {@code auto}.",
            false,
            "auto", "visible", "hidden"
        ),

        [Doc()]: "Render arrows at the top and bottom of the vertical scrollbar. Defaults to {@code false}.",
        verticalHasArrows: T_Boolean(),

        [Doc()]: "Width in pixels for the vertical scrollbar. Defaults to {@code 10} (px).",
        verticalScrollbarSize: T_Number(),

        [Doc()]: "Width in pixels for the vertical slider. Defaults to {@code verticalScrollbarSize}.",
        verticalSliderSize: T_Number(),
    }, "Configuration options for editor scrollbars");

    const EditorUnicodeHighlightOptions = T_Class("EditorUnicodeHighlightOptions", {
        [Doc()]: "A map of allowed characters ({@code true}: allowed).",
        allowedCharacters: T_Map(T_String(), T_Boolean()),

        [Doc()]: "Unicode characters that are common in allowed locales are not being highlighted. ({@code true}: allowed).",
        allowedLocales: T_Map(T_String(), T_Boolean()),

        [Doc()]: "Controls whether characters are highlighted that can be confused with basic ASCII characters, except those that are common in the current user locale.",
        ambiguousCharacters: T_Boolean(),

        [Doc()]: "Controls whether characters in comments should also be subject to unicode highlighting. Supported string constants: {@code inUntrustedWorkspace}.",
        includeComments: T_BooleanOrString(),

        [Doc()]: "Controls whether characters in strings should also be subject to unicode highlighting. Supported string constants: {@code inUntrustedWorkspace}.",
        includeStrings: T_BooleanOrString(),

        [Doc()]: "Controls whether characters that just reserve space or have no width at all are highlighted.",
        invisibleCharacters: T_Boolean(),

        [Doc()]: "Controls whether all non-basic ASCII characters are highlighted. Only characters between U+0020 and U+007E, tab, line-feed and carriage-return are considered basic ASCII. Supported string constants: {@code inUntrustedWorkspace}.",
        nonBasicASCII: T_BooleanOrString(),
    }, "Defines how Unicode characters should be highlighted.");

    const EditorSuggestOptions = T_Class("EditorSuggestOptions", {
        [Doc()]: "Enable graceful matching. Defaults to {@code true}.",
        filterGraceful: T_Boolean(),

        [Doc()]: "Overwrite word ends on accept. Default to {@code false}.",
        insertMode: T_Enum("EInsertMode",
            "Overwrite word ends on accept. Default to {@code false}.",
            false,
            "insert", "replace"
        ),

        [Doc()]: "Favours words that appear close to the cursor.",
        localityBonus: T_Boolean(),

        [Doc()]: "Controls whether suggestions allow matches in the middle of the word instead of only at the beginning.",
        matchOnWordStartOnly: T_Boolean(),

        [Doc()]: "Enable or disable the rendering of the suggestion preview.",
        preview: T_Boolean(),

        [Doc()]: "Configures the mode of the preview.",
        previewMode: T_Enum("ESuggestPreviewMode",
            "Configures the mode of the preview.",
            false,
            "prefix", "subword", "subwordSmart"
        ),

        [Doc()]: "Select suggestions when triggered via quick suggest or trigger characters.",
        selectionMode: T_Enum("ESuggestSelectionMode",
            "Select suggestions when triggered via quick suggest or trigger characters.",
            false,
            "always", "never", "whenTriggerCharacter", "whenQuickSuggestion"
        ),

        [Doc()]: "Enable using global storage for remembering suggestions.",
        shareSuggestSelections: T_Boolean(),

        [Doc()]: "Show class-suggestions.",
        showClasses: T_Boolean(),

        [Doc()]: "Show color-suggestions.",
        showColors: T_Boolean(),

        [Doc()]: "Show constant-suggestions.",
        showConstants: T_Boolean(),

        [Doc()]: "Show constructor-suggestions.",
        showConstructors: T_Boolean(),

        [Doc()]: "Show deprecated-suggestions.",
        showDeprecated: T_Boolean(),

        [Doc()]: "Show enumMember-suggestions.",
        showEnumMembers: T_Boolean(),

        [Doc()]: "Show enum-suggestions.",
        showEnums: T_Boolean(),

        [Doc()]: "Show event-suggestions.",
        showEvents: T_Boolean(),

        [Doc()]: "Show field-suggestions.",
        showFields: T_Boolean(),

        [Doc()]: "Show file-suggestions.",
        showFiles: T_Boolean(),

        [Doc()]: "Show folder-suggestions.",
        showFolders: T_Boolean(),

        [Doc()]: "Show function-suggestions.",
        showFunctions: T_Boolean(),

        [Doc()]: "Enable or disable icons in suggestions. Defaults to {@code true}.",
        showIcons: T_Boolean(),

        [Doc()]: "Show details inline with the label. Defaults to {@code true}.",
        showInlineDetails: T_Boolean(),

        [Doc()]: "Show interface-suggestions.",
        showInterfaces: T_Boolean(),

        [Doc()]: "Show issue-suggestions.",
        showIssues: T_Boolean(),

        [Doc()]: "Show keyword-suggestions.",
        showKeywords: T_Boolean(),

        [Doc()]: "Show method-suggestions.",
        showMethods: T_Boolean(),

        [Doc()]: "Show module-suggestions.",
        showModules: T_Boolean(),

        [Doc()]: "Show operator-suggestions.",
        showOperators: T_Boolean(),

        [Doc()]: "Show property-suggestions.",
        showProperties: T_Boolean(),

        [Doc()]: "Show reference-suggestions.",
        showReferences: T_Boolean(),

        [Doc()]: "Show snippet-suggestions.",
        showSnippets: T_Boolean(),

        [Doc()]: "Enable or disable the suggest status bar.",
        showStatusBar: T_Boolean(),

        [Doc()]: "Show struct-suggestions.",
        showStructs: T_Boolean(),

        [Doc()]: "Show typeParameter-suggestions.",
        showTypeParameters: T_Boolean(),

        [Doc()]: "Show unit-suggestions.",
        showUnits: T_Boolean(),

        [Doc()]: "Show user-suggestions.",
        showUsers: T_Boolean(),

        [Doc()]: "Show value-suggestions.",
        showValues: T_Boolean(),

        [Doc()]: "Show variable-suggestions.",
        showVariables: T_Boolean(),

        [Doc()]: "Show text-suggestions.",
        showWords: T_Boolean(),

        [Doc()]: "Prevent quick suggestions when a snippet is active. Defaults to {@code true}.",
        snippetsPreventQuickSuggestions: T_Boolean(),
    }, "Configuration options for editor suggest widget");

    const EditorDimension = T_Class("EditorDimension", {
        height: T_Number(),
        width: T_Number(),
    }, "The initial editor dimension (to avoid measuring the container).");

    const EditorBracketPairColorizationOptions = T_Class("EditorBracketPairColorizationOptions", {
        [Doc()]: "Enable or disable bracket pair colorization.",
        enabled: T_Boolean(),

        [Doc()]: "Use independent color pool per bracket type.",
        independentColorPoolPerBracketType: T_Boolean(),
    }, "Configures bracket pair colorization (disabled by default).");

    const EditorCommentsOptions = T_Class("EditorCommentsOptions", {
        [Doc()]: "Ignore empty lines when inserting line comments. Defaults to {@code} true.",
        ignoreEmptyLines: T_Boolean(),

        [Doc()]: "Insert a space after the line comment token and inside the block comments tokens. Defaults to {@code true}.",
        insertSpace: T_Boolean(),
    }, "Configuration options for editor comments");

    const EditorDropIntoEditorOptions = T_Class("EditorDropIntoEditorOptions", {
        [Doc()]: "Enable the dropping into editor. Defaults to {@code true}.",
        enabled: T_Boolean(),
    }, "Configuration options for editor drop into behavior");

    const QuickSuggestionsValue = T_BooleanOrEnum(
        "EQuickSuggestionsValue",
        "Whether to include comments in quick suggestions (shadow suggestions)",
        false,
        "on", "inline", "off"
    );

    const EditorQuickSuggestionsOptions = T_Class("EditorQuickSuggestionsOptions", {
        [Doc()]: "Whether to include comments in quick suggestions (shadow suggestions)",
        comments: QuickSuggestionsValue,

        [Doc()]: "Whether to include other types in quick suggestions (shadow suggestions)",
        other: QuickSuggestionsValue,

        [Doc()]: "Whether to include strings in quick suggestions (shadow suggestions)",
        strings: QuickSuggestionsValue,
    }, "Configuration options for quick suggestions");

    const DiffEditorSpecificOptions = {
        [Doc()]: "The initial editor dimension (to avoid measuring the container).",
        dimension: EditorDimension,

        diffAlgorithm: T_Enum("EDiffAlgorithm",
            "Controls the diff algorithm.",
            false,

            [Doc("The default algorithm.")],
            "smart",

            [Doc("An improved experimental algorithm.")],
            "experimental",
        ),

        [Doc()]: "Controls the wrapping of the diff editor.",
        diffWordWrap: T_Enum("EDiffWordWrap",
            "Controls the wrapping of the diff editor.",
            false,

            [Doc("Wrap words.")],
            "on",

            [Doc("Do not wrap words.")],
            "off",

            [Doc("Inherit from the parent DOM element.")],
            "inherit",
        ),

        [Doc()]: "Initial theme to be used for rendering. The current out-of-the-box available themes are: {@code vs} (default), {@code vs-dark}, {@code hc-black}. You can create custom themes via {@code monaco.editor.defineTheme}. To switch a theme, use {@code monaco.editor.setTheme}",
        theme: ETheme,

        [Doc()]: "Should the diff editor enable code lens? Defaults to {@code false}.",
        diffCodeLens: T_Boolean(),

        [Doc()]: "Allow the user to resize the diff editor split view. Defaults to {@code true}.",
        enableSplitViewResizing: T_Boolean(),

        [Doc()]: "Compute the diff by ignoring leading/trailing whitespace Defaults to {@code true}.",
        ignoreTrimWhitespace: T_Boolean(),

        [Doc()]: "Render +/- indicators for added/deleted changes. Defaults to {@code true}.",
        renderIndicators: T_Boolean(),

        [Doc()]: "Shows icons in the glyph margin to revert changes. Default to {@code true}.",
        renderMarginRevertIcon: T_Boolean(),

        [Doc()]: "Is the diff editor should render overview ruler Defaults to {@code true}",
        renderOverviewRuler: T_Boolean(),

        [Doc()]: "Render the differences in two side-by-side editors. Defaults to {@code true}.",
        renderSideBySide: T_Boolean(),

        [Doc()]: "Timeout in milliseconds after which diff computation is cancelled. Defaults to {@code 5000}.",
        maxComputationTime: T_Number(),

        [Doc()]: "Maximum supported file size in MB. Defaults to {@code 50}.",
        maxFileSize: T_Number(),

        [Doc()]: "Aria label for modified editor.",
        modifiedAriaLabel: T_String(),

        [Doc()]: "Aria label for original editor.",
        originalAriaLabel: T_String(),
    };

    const EditorSpecificOptions = {
        [Doc()]: "The initial editor dimension (to avoid measuring the container).",
        dimension: EditorDimension,

        [Doc()]: "The initial language of the auto created model in the editor. To not create automatically a model, use {@code model: null}.",
        language: T_Enum("ELanguage",
            "The initial language of the auto created model in the editor.",
            true,

            "abap",
            "aes",
            "apex",
            "azcli",
            "bat",
            "bicep",
            "c",
            "cameligo",
            "clojure",
            "coffeescript",
            "cpp",
            "csharp",
            "csp",
            "css",
            "cypher",
            "dart",
            "dockerfile",
            "ecl",
            "elixir",
            "flow9",
            "freemarker2",
            "freemarker2.tag-angle.interpolation-bracket",
            "freemarker2.tag-angle.interpolation-dollar",
            "freemarker2.tag-auto.interpolation-bracket",
            "freemarker2.tag-auto.interpolation-dollar",
            "freemarker2.tag-bracket.interpolation-bracket",
            "freemarker2.tag-bracket.interpolation-dollar",
            "fsharp",
            "go",
            "graphql",
            "handlebars",
            "hcl",
            "html",
            "ini",
            "java",
            "javascript",
            "json",
            "julia",
            "kotlin",
            "less",
            "lexon",
            "liquid",
            "lua",
            "m3",
            "markdown",
            "mips",
            "msdax",
            "mysql",
            "objective-c",
            "pascal",
            "pascaligo",
            "perl",
            "pgsql",
            "php",
            "pla",
            "plaintext",
            "postiats",
            "powerquery",
            "powershell",
            "proto",
            "pug",
            "python",
            "qsharp",
            "r",
            "razor",
            "redis",
            "redshift",
            "restructuredtext",
            "ruby",
            "rust",
            "sb",
            "scala",
            "scheme",
            "scss",
            "shell",
            "sol",
            "sparql",
            "sql",
            "st",
            "swift",
            "systemverilog",
            "tcl",
            "twig",
            "typescript",
            "vb",
            "verilog",
            "xml",
            "yaml"
        ),

        [Doc()]: "Initial theme to be used for rendering. The current out-of-the-box available themes are: {@code vs} (default), {@code vs-dark}, {@code hc-black}. You can create custom themes via {@code monaco.editor.defineTheme}. To switch a theme, use {@code monaco.editor.setTheme}",
        theme: ETheme,

        [Doc()]: "Controls whether the semanticHighlighting is shown for the languages that support it. {@code true}: Semantic highlighting is enabled for all themes {@code false}: Semantic highlighting is disabled for all themes. {@code configuredByTheme}: Semantic highlighting is controlled by the current color theme's {@code semanticHighlighting} setting. Defaults to {@code configuredByTheme}.",
        "semanticHighlighting.enabled": T_Enum("ESemanticHighlightingEnabled",
            "Controls whether the semanticHighlighting is shown for the languages that support it. {@code true}: Semantic highlighting is enabled for all themes {@code false}: Semantic highlighting is disabled for all themes. {@code configuredByTheme}: Semantic highlighting is controlled by the current color theme's {@code semanticHighlighting} setting. Defaults to {@code configuredByTheme}.",
            false,
            "true", "false", "configuredByTheme"
        ),

        [Doc()]: "Controls whether {@code tabSize} and {@code insertSpaces} will be automatically detected when a file is opened based on the file contents. Defaults to {@code true}.",
        detectIndentation: T_Boolean(),

        [Doc()]: "Insert spaces when pressing {@code Tab}. This setting is overridden based on the file contents when {@code detectIndentation} is on. Defaults to {@code true}.",
        insertSpaces: T_Boolean(),

        [Doc()]: "Keep peek editors open even when double clicking their content or when hitting Escape. Defaults to {@code false}",
        stablePeek: T_Boolean(),

        [Doc()]: "Remove trailing auto inserted whitespace. Defaults to {@code true}.",
        trimAutoWhitespace: T_Boolean(),

        [Doc()]: "Controls whether completions should be computed based on words in the document. Defaults to {@code true}.",
        wordBasedSuggestions: T_Boolean(),

        [Doc()]: "Controls whether word based completions should be included from opened documents of the same language or any language.",
        wordBasedSuggestionsOnlySameLanguage: T_Boolean(),

        [Doc()]: "Lines above this length will not be tokenized for performance reasons. Defaults to {@code 20000}.",
        maxTokenizationLineLength: T_Number(),

        [Doc()]: "The number of spaces a tab is equal to. This setting is overridden based on the file contents when {@code detectIndentation} is on. Defaults to {@code 4}.",
        tabSize: T_Number(),

        [Doc()]: "An URL to open when Ctrl+H (Windows and Linux) or Cmd+H (OSX) is pressed in the accessibility help dialog in the editor.",
        accessibilityHelpUrl: T_String(),
    };

    const BaseEditorOptions = {
        [Doc()]: "Configures bracket pair colorization (disabled by default).",
        bracketPairColorization: EditorBracketPairColorizationOptions,

        [Doc()]: "Control the behavior of comments in the editor.",
        comments: EditorCommentsOptions,

        [Doc()]: "Controls dropping into the editor from an external source. When enabled, this shows a preview of the drop location and triggers an onDropIntoEditor event.",
        dropIntoEditor: EditorDropIntoEditorOptions,

        [Doc()]: "Control the behavior of the find widget.",
        find: EditorFindOptions,

        [Doc()]: "Controls the behavior of editor guides.",
        guides: EditorGuidesOptions,

        gotoLocation: EditorGotoLocationOptions,

        [Doc()]: "Configure the editor's hover.",
        hover: EditorHoverOptions,

        [Doc()]: "Control the behavior and rendering of the inline hints.",
        inlayHints: EditorInlayHintOptions,

        [Doc()]: "Control the behavior and rendering of the inline completions.",
        inlineSuggest: EditorInlineSuggestOptions,

        [Doc()]: "Control the behavior and rendering of the code action lightbulb.",
        lightbulb: EditorLightbulbOptions,

        [Doc()]: "Control the behavior and rendering of the minimap.",
        minimap: EditorMinimapOptions,

        [Doc()]: "Controls the spacing around the editor.",
        padding: EditorPaddingOptions,

        [Doc()]: "Parameter hint options.",
        parameterHints: EditorParameterHints,

        [Doc()]: "Enable quick suggestions (shadow suggestions) Defaults to {@code true}.",
        quickSuggestions: EditorQuickSuggestionsOptions,

        [Doc()]: "Control the behavior and rendering of the scrollbars.",
        scrollbar: EditorScrollbarOptions,

        [Doc()]: "Smart select options.",
        smartSelect: EditorSmartSelectOptions,

        [Doc()]: "Control the behavior of sticky scroll options",
        stickyScroll: EditorStickyScrollOptions,

        [Doc()]: "Suggest options.",
        suggest: EditorSuggestOptions,

        [Doc()]: "Defines how Unicode characters should be highlighted.",
        unicodeHighlight: EditorUnicodeHighlightOptions,

        [Doc()]: "Options for typing over closing quotes or brackets.",
        autoClosingOvertype: T_Enum("EAutoClosingOvertype",
            "Options for typing over closing quotes or brackets.",
            false,

            [Doc("Always type over closing quotes and brackets without inserting a new quote or bracket.")],
            "always",

            [Doc("Decide dynamically whether to to type over closing quotes and brackets or whether to insert a new quote or bracket.")],
            "auto",

            [Doc("Never type over closing quotes and brackets, always insert a new quote or bracket.")],
            "never"
        ),

        [Doc()]: "Controls whether the editor should automatically adjust the indentation when users type, paste, move or indent lines. Defaults to {@code advanced}.",
        autoIndent: T_Enum("EAutoIndent",
            "Controls whether the editor should automatically adjust the indentation when users type, paste, move or indent lines. Defaults to {@code advanced}.",
            false,
            "none", "keep", "brackets", "advanced", "full"
        ),

        [Doc()]: "Accept suggestions on ENTER. Defaults to {@code on}.",
        acceptSuggestionOnEnter: T_Enum("EAcceptSuggestionOnEnter",
            "Accept suggestions on ENTER. Defaults to {@code on}.",
            false,
            "on", "smart", "off"
        ),

        [Doc()]: "Configure the editor's accessibility support. Defaults to {@code auto}. It is best to leave this to {@code auto}.",
        accessibilitySupport: T_Enum("EAccessibilitySupport",
            "Configure the editor's accessibility support. Defaults to {@code auto}. It is best to leave this to {@code auto}.",
            false,
            "auto", "off", "on"
        ),

        [Doc()]: "Options for auto closing brackets. Defaults to language defined behavior.",
        autoClosingBrackets: T_Enum("EAutoClosingBrackets",
            "Options for auto closing brackets. Defaults to language defined behavior.",
            false,
            "always", "languageDefined", "beforeWhitespace", "never"
        ),

        [Doc()]: "Options for pressing backspace near quotes or bracket pairs.",
        autoClosingDelete: T_Enum("EAutoClosingEditStrategy",
            "Options for pressing backspace near quotes or bracket pairs.",
            false,
            "always", "auto", "never"
        ),

        [Doc()]: "Options for auto closing quotes. Defaults to language defined behavior.",
        autoClosingQuotes: T_Enum("EAutoClosingQuotes",
            "Options for auto closing quotes. Defaults to language defined behavior.",
            false,
            "always", "languageDefined", "beforeWhitespace", "never"
        ),

        [Doc()]: "Options for auto surrounding. Defaults to always allowing auto surrounding.",
        autoSurround: T_Enum("EAutoSurround",
            "Options for auto surrounding. Defaults to always allowing auto surrounding.",
            false,
            "languageDefined", "quotes", "brackets", "never"
        ),

        [Doc()]: "Control the cursor animation style, possible values are {@code blink}, {@code smooth}, {@code phase}, {@code expand} and {@code solid}. Defaults to {@code blink}.",
        cursorBlinking: T_Enum("ECursorBlinking",
            "Control the cursor animation style, possible values are {@code blink}, {@code smooth}, {@code phase}, {@code expand} and {@code solid}. Defaults to {@code blink}.",
            false,
            "blink", "smooth", "phase", "expand", "solid"
        ),

        [Doc()]: "Enable smooth caret animation. Defaults to {@code off}.",
        cursorSmoothCaretAnimation: T_Enum("ECursorSmoothCaretAnimation",
            "Enable smooth caret animation. Defaults to {@code off}.",
            false,
            "on", "off", "explicit"
        ),

        [Doc()]: "Control the cursor style, either {@code block} or {@code line}. Defaults to {@code line}.",
        cursorStyle: T_Enum("ECursorStyle",
            "Control the cursor style, either {@code block} or {@code line}. Defaults to {@code line}.",
            false,
            "block", "line", "underline", "line-thin", "block-outline", "underline-thin"
        ),

        [Doc()]: "Controls when {@code cursorSurroundingLines} should be enforced Defaults to {@code default}, {@code cursorSurroundingLines} is not enforced when cursor position is changed by mouse.",
        cursorSurroundingLinesStyle: T_Enum("ECursorSurroundingLinesStyle",
            "Controls when {@code cursorSurroundingLines} should be enforced Defaults to {@code default}, {@code cursorSurroundingLines} is not enforced when cursor position is changed by mouse.",
            false,
            "default", "all"
        ),

        [Doc()]: "Enable experimental whitespace rendering. Defaults to {@code svg}.",
        experimentalWhitespaceRendering: T_Enum("EFoldingStrategy",
            "Enable experimental whitespace rendering. Defaults to {@code svg}.",
            false,
            "off", "svg", "font"
        ),

        [Doc()]: "Selects the folding strategy. 'auto' uses the strategies contributed for the current document, 'indentation' uses the indentation based folding strategy. Defaults to 'auto'.",
        foldingStrategy: T_Enum("EFoldingStrategy",
            "Selects the folding strategy. 'auto' uses the strategies contributed for the current document, 'indentation' uses the indentation based folding strategy. Defaults to 'auto'.",
            false,
            "auto", "indentation"
        ),

        [Doc()]: "The font weight",
        fontWeight: T_Enum("EFontWeight",
            "The font weight",
            false,
            "normal", "bold", "bolder", "lighter",
            "initial", "inherit",
            "100", "200", "300", "400", "500", "600", "700", "800", "900"
        ),

        [Doc()]: "Control the rendering of line numbers. If it is a function, it will be invoked when rendering a line number and the return value will be rendered. Otherwise, if it is a truey, line numbers will be rendered normally (equivalent of using an identity function). Otherwise, line numbers will not be rendered. Defaults to {@code on}.",
        lineNumbers: T_Enum("ELineNumbers",
            "Control the rendering of line numbers. If it is a function, it will be invoked when rendering a line number and the return value will be rendered. Otherwise, if it is a truey, line numbers will be rendered normally (equivalent of using an identity function). Otherwise, line numbers will not be rendered. Defaults to {@code on}.",
            false,
            "on", "off", "relative", "interval"
        ),

        [Doc()]: "Enable highlighting of matching brackets. Defaults to {@code always}.",
        matchBrackets: T_Enum("EMatchBrackets", "Enable highlighting of matching brackets. Defaults to {@code always}.",
            false,
            "never", "near", "always"
        ),

        [Doc()]: "Control the mouse pointer style, either 'text' or 'default' or 'copy' Defaults to {@code text}",
        mouseStyle: T_Enum("EMouseStyle",
            "Control the mouse pointer style, either 'text' or 'default' or 'copy' Defaults to {@code text}",
            false,
            "text", "default", "copy"
        ),

        [Doc()]: "The modifier to be used to add multiple cursors with the mouse. Defaults to {@code alt}",
        multiCursorModifier: T_Enum("EMultiCursorModifier",
            "The modifier to be used to add multiple cursors with the mouse. Defaults to {@code alt}",
            false,
            "ctrlCmd", "alt"
        ),

        [Doc()]: "Configure the behaviour when pasting a text with the line count equal to the cursor count. Defaults to {@code spread}.",
        multiCursorPaste: T_Enum("EMultiCursorPaste",
            "Configure the behaviour when pasting a text with the line count equal to the cursor count. Defaults to {@code spread}.",
            false,
            "spread", "full"
        ),

        [Doc()]: "Controls whether to focus the inline editor in the peek widget by default. Defaults to {@code false}.",
        peekWidgetDefaultFocus: T_Enum("EPeekWidgetDefaultFocus",
            "Controls whether to focus the inline editor in the peek widget by default. Defaults to {@code false}.",
            false,
            "tree", "editor"
        ),

        [Doc()]: "Render last line number when the file ends with a newline. Defaults to {@code on} for Windows and macOS and {@code dimmed} for Linux.",
        renderFinalNewline: T_Enum("ERenderFinalNewline",
            "Render last line number when the file ends with a newline. Defaults to {@code on} for Windows and macOS and {@code dimmed} for Linux.",
            false,
            "on", "off", "dimmed"
        ),

        [Doc()]: "Enable rendering of current line highlight. Defaults to {@code all}.",
        renderLineHighlight: T_Enum("ERenderLineHighlight",
            "Enable rendering of current line highlight. Defaults to {@code all}.",
            false,
            "none", "gutter", "line", "all"
        ),

        [Doc()]: "Should the editor render validation decorations. Defaults to {@code editable}.",
        renderValidationDecorations: T_Enum("ERenderValidationDecorations",
            "Should the editor render validation decorations. Defaults to {@code editable}.",
            false,
            "editable", "on", "off"
        ),

        [Doc()]: "Enable rendering of whitespace. Defaults to {@code none}.",
        renderWhitespace: T_Enum("ERenderWhitespace",
            "Enable rendering of whitespace. Defaults to {@code none}.",
            false,
            "none", "boundary", "selection", "trailing", "all"
        ),

        [Doc()]: "Controls whether the fold actions in the gutter stay always visible or hide unless the mouse is over the gutter. Defaults to {@code mouseover}.",
        showFoldingControls: T_Enum("EShowFoldingControls",
            "Controls whether the fold actions in the gutter stay always visible or hide unless the mouse is over the gutter. Defaults to {@code mouseover}.",
            false,
            "always", "never", "mouseover"
        ),

        [Doc()]: "Enable snippet suggestions. Defaults to 'inline'.",
        snippetSuggestions: T_Enum("ESnippetSuggestions",
            "Enable snippet suggestions. Defaults to 'inline'.",
            false,
            "top", "bottom", "inline", "none"
        ),

        [Doc()]: "Keep peek editors open even when double clicking their content or when hitting Escape. Defaults to {@code false}.",
        suggestSelection: T_Enum("ESuggestSelection",
            "Keep peek editors open even when double clicking their content or when hitting Escape. Defaults to {@code false}.",
            false,
            "first", "recentlyUsed", "recentlyUsedByPrefix"
        ),

        [Doc()]: "Enable tab completion.",
        tabCompletion: T_Enum("ETabCompletion",
            "Enable tab completion.",
            false,
            "on", "off", "onlySnippets"
        ),

        [Doc()]: "Remove unusual line terminators like LINE SEPARATOR (LS), PARAGRAPH SEPARATOR (PS). Defaults to {@code prompt}.",
        unusualLineTerminators: T_Enum("EUnusualLineTerminators",
            "Remove unusual line terminators like LINE SEPARATOR (LS), PARAGRAPH SEPARATOR (PS). Defaults to {@code prompt}.",
            false,
            "off", "prompt", "auto"
        ),

        [Doc()]: "Sets whether line breaks appear wherever the text would otherwise overflow its content box. When wordBreak = {@code normal}, Use the default line break rule. When wordBreak = {@code keepAll}, word breaks should not be used for Chinese/Japanese/Korean (CJK) text. Non-CJK text behavior is the same as for normal.",
        wordBreak: T_Enum("EWordBreak",
            "Sets whether line breaks appear wherever the text would otherwise overflow its content box. When wordBreak = {@code normal}, Use the default line break rule. When wordBreak = {@code keepAll}, word breaks should not be used for Chinese/Japanese/Korean (CJK) text. Non-CJK text behavior is the same as for normal.",
            false,
            "normal", "keepAll"
        ),

        [Doc()]: "Control the wrapping of the editor. When {@code wordWrap} = {@code off}, the lines will never wrap. When {@code wordWrap} = {@code on}, the lines will wrap at the viewport width. When {@code wordWrap} = {@code wordWrapColumn}, the lines will wrap at {@code wordWrapColumn}. When {@code wordWrap} = {@code bounded}, the lines will wrap at {@code min(viewport width, wordWrapColumn)}. Defaults to {@code off}.",
        wordWrap: T_Enum("EWordWrap",
            "Control the wrapping of the editor. When {@code wordWrap} = {@code off}, the lines will never wrap. When {@code wordWrap} = {@code on}, the lines will wrap at the viewport width. When {@code wordWrap} = {@code wordWrapColumn}, the lines will wrap at {@code wordWrapColumn}. When {@code wordWrap} = {@code bounded}, the lines will wrap at {@code min(viewport width, wordWrapColumn)}. Defaults to {@code off}.",
            false,
            "off", "on", "wordWrapColumn", "bounded"
        ),

        [Doc()]: "Override the {@code wordWrap} setting.",
        wordWrapOverride1: T_Enum("EWordWrapOverride1",
            "Override the {@code wordWrap} setting.",
            false,
            "on", "off", "inherit"
        ),

        [Doc()]: "Override the {@code wordWrapOverride1} setting.",
        wordWrapOverride2: T_Enum("EWordWrapOverride2",
            "Override the {@code wordWrapOverride1} setting.",
            false,
            "on", "off", "inherit"
        ),

        [Doc()]: "Control indentation of wrapped lines. Can be: {@code none}, {@code same}, {@code indent} or {@code deepIndent}. Defaults to {@code same} in vscode and to {@code none} in monaco-editor.",
        wrappingIndent: T_Enum("EWrappingIndent",
            "Control indentation of wrapped lines. Can be: {@code none}, {@code same}, {@code indent} or {@code deepIndent}. Defaults to {@code same} in vscode and to {@code none} in monaco-editor.",
            false,
            "none", "same", "indent", "deepIndent"
        ),

        [Doc()]: "Controls the wrapping strategy to use. Defaults to {@code simple}.",
        wrappingStrategy: T_Enum("EWrappingStrategy",
            "Controls the wrapping strategy to use. Defaults to {@code simple}.",
            false,
            "simple", "advanced"
        ),

        [Doc()]: "Render vertical lines at the specified columns. Defaults to empty array.",
        rulers: T_Array(EditorRulerOption),

        [Doc()]: "Accept suggestions on provider defined characters. Defaults to {@code true}.",
        acceptSuggestionOnCommitCharacter: T_Boolean(),

        [Doc()]: "If enabled, will automatically change to high contrast theme if the OS is using a high contrast theme. Defaults to {@code true}.",
        autoDetectHighContrast: T_Boolean(),

        [Doc()]: "Enable that the editor will install an interval to check if its container dom node size has changed. Enabling this might have a severe performance impact. Defaults to {@code false}.",
        automaticLayout: T_Boolean(),

        [Doc()]: "Show code lens Defaults to {@code true}.",
        codeLens: T_Boolean(),

        [Doc()]: "Enable inline color decorators and color picker rendering.",
        colorDecorators: T_Boolean(),

        [Doc()]: "Controls the max number of color decorators that can be rendered in an editor at once.",
        colorDecoratorsLimit: T_Boolean(),

        [Doc()]: "Enable that the selection with the mouse and keys is doing column selection. Defaults to {@code false}.",
        columnSelection: T_Boolean(),

        [Doc()]: "Enable custom contextmenu. Defaults to {@code true}.",
        contextmenu: T_Boolean(),

        [Doc()]: "Syntax highlighting is copied.",
        copyWithSyntaxHighlighting: T_Boolean(),

        [Doc()]: "Controls whether the definition link opens element in the peek widget. Defaults to {@code false}.",
        definitionLinkOpensInPeek: T_Boolean(),

        [Doc()]: "Disable the use of transform: translate3d(0px, 0px, 0px) for the editor margin and lines layers. The usage of transform: translate3d(0px, 0px, 0px) acts as a hint for browsers to create an extra layer. Defaults to {@code false}.",
        disableLayerHinting: T_Boolean(),

        [Doc()]: "Disable the optimizations for monospace fonts. Defaults to {@code false}.",
        disableMonospaceOptimizations: T_Boolean(),

        [Doc()]: "Controls if the editor should allow to move selections via drag and drop. Defaults to {@code false}.",
        dragAndDrop: T_Boolean(),

        [Doc()]: "Copying without a selection copies the current line",
        emptySelectionClipboard: T_Boolean(),

        [Doc()]: "Display overflow widgets as {@code fixed}. Defaults to {@code false}",
        fixedOverflowWidgets: T_Boolean(),

        [Doc()]: "Enable code folding. Defaults to {@code true}.",
        folding: T_Boolean(),

        [Doc()]: "Enable highlight for folded regions. Defaults to {@code true}.",
        foldingHighlight: T_Boolean(),

        [Doc()]: "Auto fold imports folding regions. Defaults to {@code true}.",
        foldingImportsByDefault: T_Boolean(),

        [Doc()]: "Enable font ligatures. Defaults to {@code false}",
        fontLigatures: T_BooleanOrString(),

        [Doc()]: "Enable font variations. Defaults to {@code false}",
        fontVariations: T_BooleanOrString(),

        [Doc()]: "Enable format on paste. Defaults to {@code false}",
        formatOnPaste: T_Boolean(),

        [Doc()]: "Enable format on type. Defaults to {@code false}",
        formatOnType: T_Boolean(),

        [Doc()]: "Enable the rendering of the glyph margin. Defaults to {@code true}. in vscode and to {@code false} in monaco-editor",
        glyphMargin: T_Boolean(),

        [Doc()]: "Should the cursor be hidden in the overview ruler. Defaults to {@code false}",
        hideCursorInOverviewRuler: T_Boolean(),

        [Doc()]: "Special handling for large files to disable certain memory intensive features. Defaults to {@code true}.",
        largeFileOptimizations: T_Boolean(),

        [Doc()]: "Enables linked editing. Defaults to {@code false}.",
        linkedEditing: T_Boolean(),

        [Doc()]: "Enable detecting links and making them clickable. Defaults to {@code true}.",
        links: T_Boolean(),

        [Doc()]: "Controls whether suggestions allow matches in the middle of the word instead of only at the beginning",
        matchOnWordStartOnly: T_Boolean(),

        [Doc()]: "Zoom the font in the editor when using the mouse wheel in combination with holding Ctrl. Defaults to {@code false}",
        mouseWheelZoom: T_Boolean(),

        [Doc()]: "Merge overlapping selections. Defaults to {@code true}.",
        multiCursorMergeOverlapping: T_Boolean(),

        [Doc()]: "Enable semantic occurrences highlight. Defaults to {@code true}.",
        occurrencesHighlight: T_Boolean(),

        [Doc()]: "Controls if a border should be drawn around the overview ruler. Defaults to {@code true}.",
        overviewRulerBorder: T_Boolean(),

        [Doc()]: "Should the editor be read only. Defaults to {@code false}",
        readOnly: T_Boolean(),

        [Deprecated()]: "",
        [Doc()]: "Deprecated, use {@link linkedEditing} instead.",
        renameOnType: T_Boolean(),

        [Doc()]: "Enable rendering of control characters. Defaults to {@code true}.",
        renderControlCharacters: T_Boolean(),

        [Doc()]: "Control if the current line highlight should be rendered only the editor is focused. Defaults to {@code false}.",
        renderLineHighlightOnlyWhenFocus: T_Boolean(),

        [Doc()]: "Render the editor selection with rounded borders. Defaults to {@code true}.",
        roundedSelection: T_Boolean(),

        [Doc()]: "Enable that scrolling can go one screen size after the last line. Defaults to {@code true}.",
        scrollBeyondLastLine: T_Boolean(),

        [Doc()]: "Enable that the editor scrolls only the predominant axis. Prevents horizontal drift when scrolling vertically on a trackpad. Defaults to {@code true}.}",
        scrollPredominantAxis: T_Boolean(),

        [Doc()]: "Should the corresponding line be selected when clicking on the line number? Defaults to {@code true}.",
        selectOnLineNumbers: T_Boolean(),

        [Doc()]: "Enable Linux primary clipboard. Defaults to {@code true}.",
        selectionClipboard: T_Boolean(),

        [Doc()]: "Enable selection highlight. Defaults to {@code true}.",
        selectionHighlight: T_Boolean(),

        [Doc()]: "Controls strikethrough deprecated variables.",
        showDeprecated: T_Boolean(),

        [Doc()]: "Controls fading out of unused variables",
        showUnused: T_Boolean(),

        [Doc()]: "Enable that the editor animates scrolling to a position. Defaults to {@code false}",
        smoothScrolling: T_Boolean(),

        [Doc()]: "Keep peek editors open even when double clicking their content or when hitting Escape. Defaults to {@code false}",
        stablePeek: T_Boolean(),

        [Doc()]: "Enable the suggestion box to pop-up on trigger characters. Defaults to {@code true}.",
        suggestOnTriggerCharacters: T_Boolean(),

        [Doc()]: "Controls whether clicking on the empty content after a folded line will unfold the line. Defaults to {@code false}.",
        unfoldOnClickAfterEndOfLine: T_Boolean(),

        [Doc()]: "Control if the editor should use shadow DOM.",
        useShadowDOM: T_Boolean(),

        [Doc()]: "Inserting and deleting whitespace follows tab stops.",
        useTabStops: T_Boolean(),

        [Doc()]: "Force word wrapping when the text appears to be of a minified/generated file. Defaults to {@code true}.",
        wordWrapMinified: T_Boolean(),

        [Doc()]: "Controls the number of lines in the editor that can be read out by a screen reader.",
        accessibilityPageSize: T_Number(),

        [Doc()]: "Timeout for running code actions on save.",
        codeActionsOnSaveTimeout: T_Number(),

        [Doc()]: "Code lens font size. Default to 90% of the editor font size.",
        codeLensFontSize: T_Number(),

        [Doc()]: "Controls the minimal number of visible leading and trailing lines surrounding the cursor. Defaults to {@code 0}.",
        cursorSurroundingLines: T_Number(),

        [Doc()]: "Control the width of the cursor when cursorStyle is set to {@code line}",
        cursorWidth: T_Number(),

        [Doc()]: "Fast scrolling multiplier speed when pressing {@code Alt} Defaults to {@code 5}.",
        fastScrollSensitivity: T_Number(),

        [Doc()]: "Maximum number of foldable regions. Defaults to {@code 5000}.",
        foldingMaximumRegions: T_Number(),

        [Doc()]: "The font size",
        fontSize: T_Number(),

        [Doc()]: "This editor is used inside a diff editor.",
        inDiffEditor: T_Boolean(),

        [Doc()]: "Emulate selection behaviour of tab characters when using spaces for indentation. This means selection will stick to tab stops.",
        stickyTabStops: T_Boolean(),

        [Doc()]: "The letter spacing",
        letterSpacing: T_Number(),

        [Doc()]: "The line height",
        lineHeight: T_Number(),

        [Doc()]: "Control the rendering of line numbers. If it is a function, it will be invoked when rendering a line number and the return value will be rendered. Otherwise, if it is a truey, line numbers will be rendered normally (equivalent of using an identity function). Otherwise, line numbers will not be rendered. Defaults to {@code on}.",
        lineNumbersMinChars: T_Number(),

        [Doc()]: "A multiplier to be used on the {@code deltaX} and {@code deltaY} of mouse wheel scroll events. Defaults to {@code 1}.",
        mouseWheelScrollSensitivity: T_Number(),

        [Doc()]: "Controls the max number of text cursors that can be in an active editor at once.",
        multiCursorLimit: T_Number(),

        [Doc()]: "The number of vertical lanes the overview ruler should render. Defaults to {@code 3}.",
        overviewRulerLanes: T_Number(),

        [Doc()]: "Quick suggestions show delay (in ms) Defaults to {@code 10} (ms)",
        quickSuggestionsDelay: T_Number(),

        [Doc()]: "When revealing the cursor, a virtual padding (px) is added to the cursor, turning it into a rectangle. This virtual padding ensures that the cursor gets revealed before hitting the edge of the viewport. Defaults to {@code 30} (px).",
        revealHorizontalRightPadding: T_Number(),

        [Doc()]: "Enable that scrolling can go beyond the last column by a number of columns. Defaults to {@code 5}.",
        scrollBeyondLastColumn: T_Number(),

        [Doc()]: "Performance guard: Stop rendering a line after x characters. Defaults to {@code 10000}. Use {@code -1} to never stop rendering",
        stopRenderingLineAfter: T_Number(),

        [Doc()]: "The font size for the suggest widget. Defaults to the editor font size.",
        suggestFontSize: T_Number(),

        [Doc()]: "The line height for the suggest widget. Defaults to the editor line height.",
        suggestLineHeight: T_Number(),

        [Doc()]: "The {@code tabindex} property of the editor's textarea.",
        tabIndex: T_Number(),

        [Doc()]: "Control the wrapping of the editor. When {@code wordWrap} = {@code off}, the lines will never wrap. When {@code wordWrap} = {@code on}, the lines will wrap at the viewport width. When {@code wordWrap} = {@code wordWrapColumn}, the lines will wrap at wordWrapColumn. When {@code wordWrap} = {@code bounded}, the lines will wrap at min(viewport width, wordWrapColumn). Defaults to {@code 80}.",
        wordWrapColumn: T_Number(),

        [Doc()]: "The width reserved for line decorations (in px). Line decorations are placed between line numbers and the editor content. You can pass in a string in the format floating point followed by {@code ch}. e.g. 1.3ch. Defaults to 10.",
        lineDecorationsWidth: T_CssSize(),

        [Doc()]: "The aria label for the editor's textarea (when it is focused).",
        ariaLabel: T_String(),

        [Doc()]: "Code lens font family. Defaults to editor font family.",
        codeLensFontFamily: T_String(),

        [Doc()]: "Class name to be added to the editor.",
        extraEditorClassName: T_String(),

        [Doc()]: "The font family",
        fontFamily: T_String(),

        [Doc()]: "A string containing the word separators used when doing word navigation. Defaults to `~!@#$%^&*()-=+[{]}\|;:'\",.<>/?",
        wordSeparators: T_String(),

        [Doc()]: "Configure word wrapping characters. A break will be introduced after these characters. Defaults to tab stops, closing brackets and parentheses, and several other Unicode punctuation marks.",
        wordWrapBreakAfterCharacters: T_String(),

        [Doc()]: "Configure word wrapping characters. A break will be introduced before these characters. Defaults to opening brackets and parentheses, and several other Unicode punctuation marks.",
        wordWrapBreakBeforeCharacters: T_String(),
    };

    const EditorOptions = T_Class("EditorOptions", {
        ...BaseEditorOptions,
        ...EditorSpecificOptions
    }, "The options to create a Monaco Code editor instance.");

    const DiffEditorOptions = T_Class("DiffEditorOptions", {
        ...BaseEditorOptions,
        ...DiffEditorSpecificOptions
    }, "The options to create a Monaco Code diff editor instance.");

    const EditorTokenThemeRule = T_Class("EditorTokenThemeRule", {
        [Doc()]: "CSS color for the background that is applied to these tokens.",
        background: T_String(),

        [Doc()]: "Value for the CSS font-style property that is applied to these tokens.",
        fontStyle: T_String(),

        [Doc()]: "CSS color for the text color that is applied to these tokens.",
        foreground: T_String(),

        [Doc()]: "Type of token to which to apply these rules. Can be suffixed with the language ID to which they should apply. E.g. {@code comment} would apply to comment tokens of all languages, {@code comment.js} would only apply to JavaScript comment tokens. You can inspect the tokens in the Monaco editor. Right click on the editor and choose {@code Command Palette}. Then search for {@code Developer: Inspect Tokens}.",
        token: T_String(),
    }, "Defines how to style a certain token in the Monaco code editor.");

    const EditorStandaloneTheme = T_Class("EditorStandaloneTheme", {
        [Doc()]: "Base theme from which to extend when {@inherit} is set to {@code true}. This is required, when not set it defaults to {@code vs}.",
        base: ETheme,

        [Doc()]: "Map between the color ID and the CSS color to use. This is required, when not set, it defaults to an empty map.",
        colors: T_Map(T_String(), T_String()),

        [Doc()]: "Optional list of encoded token colors.",
        encodedTokensColors: T_Array(T_String()),

        [Doc()]: "Whether this theme should inherit from the given base theme. This is required, when not set it defaults to {@code false}.",
        inherit: T_Boolean(),

        [Doc()]: "Styling options for individual token types, such as how to style variables, certain keywords, and parentheses. This is required, when not set it defaults to an empty array.",
        rules: T_Array(EditorTokenThemeRule),
    }, "Data that defines a custom theme for the Monaco code editor");
}

main();