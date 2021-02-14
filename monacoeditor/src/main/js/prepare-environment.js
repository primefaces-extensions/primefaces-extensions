// The ESM version of the editor will no longer define a global monaco object.
// We can use this if we want for the editor to define this global object.
// https://github.com/microsoft/monaco-editor/blob/master/CHANGELOG.md#0220-29012021
window.MonacoEnvironment = window.MonacoEnvironment || {};
window.MonacoEnvironment.globalAPI = true;
