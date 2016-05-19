# This is buggy do not use
fs = require('graceful-fs-extra')
mkdirp = require 'mkdirp'
rimraf = require 'rimraf'
JSONStream = require 'JSONStream'
es = require 'event-stream'
require 'datejs'
LineByLineReader = require('line-by-line')



NEW_LINE_DELIMITER = '\n'
inputFolder = process.argv[2]
outputFolder = process.argv[3]

console.log "using input #{inputFolder}"

countLines = (infile, cb) ->
  fileContent = fs.readFileSync(infile)
  if(fileContent) 
    lineCount = fileContent.toString().split(NEW_LINE_DELIMITER)
    if(lineCount and lineCount.length > 0)
      numberOfLines = lineCount.length - 1
      cb(numberOfLines)

    else
      cb(0)

  else
    cb(0)
    
    
# processFile = (aFile) ->
#   console.log "processing #{aFile}"
#   lineReader = require('readline').createInterface({input: fs.createReadStream("#{inputFolder}/#{aFile}")})
#   countLines "#{inputFolder}/#{aFile}", (numberOfLines) ->
#     console.log " #{aFile} has line #{numberOfLines}"
#     lc = 0
#     lineReader.on 'line', (line) ->
#       if lc == 0
#         result = "{#{line},\n"
#       else if lc < numberOfLines
#         result = "#{line},\n"
#       else if lc >= numberOfLines
#         result = "#{line}]\n"
#       lc++
#       #console.log "#{result}"
#       fs.appendFile  "#{outputFolder}/#{aFile}", "#{lc} : #{result}",  (err) ->
#         throw err if err
#

processFile = (aFile) ->
  console.log "processing #{aFile}"
  lineReader = require('readline').createInterface({input: fs.createReadStream("#{inputFolder}/#{aFile}")})
  lr = new LineByLineReader("#{inputFolder}/#{aFile}")
  countLines "#{inputFolder}/#{aFile}", (numberOfLines) ->
    console.log " #{aFile} has line #{numberOfLines}"
    lc = 0
    lr.on 'line',  (line) ->
      if lc == 0
        result = "[#{line},\n"
      else if lc < (numberOfLines-1)
        result = "#{line},\n"
      else if lc == (numberOfLines-1)
        result = "#{line}]\n"
      lc++ 
      #console.log "#{result}"
      fs.appendFile  "#{outputFolder}/#{aFile}", "#{result}",  (err) ->
        throw err if err
  



rimraf outputFolder, (err) ->    
  mkdirp outputFolder,(err) ->
    fs.readdir inputFolder, (err,files) ->
      
      for aFile in files when !fs.lstatSync("#{inputFolder}/#{aFile}").isDirectory() and aFile.substring(0,1) isnt "."
         
         processFile(aFile)
          
