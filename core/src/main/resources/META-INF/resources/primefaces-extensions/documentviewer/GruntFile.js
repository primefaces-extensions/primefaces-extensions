module.exports = function(grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON('package.json'),
    dataUri: {
    dist: {
      // src file 
      src: ['viewer.css'],
      // output dir 
      dest: 'dist/css',
      options: {
        // specified files are only encoding 
        target: ['./images/*.*'],
        // adjust relative path? 
        fixDirLevel: true,
        // img detecting base dir 
        // baseDir: './' 
 
        // Do not inline any images larger 
        // than this size. 2048 is a size 
        // recommended by Google's mod_pagespeed. 
        maxBytes : 32048
 
      }
    }
	}
  });

  grunt.loadNpmTasks('grunt-data-uri');

  grunt.registerTask('default', ['dataUri']);

};