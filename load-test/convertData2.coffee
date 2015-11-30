fs = require 'fs'
mkdirp = require 'mkdirp'
rimraf = require 'rimraf'

# lets improve this ..
inputFolder = process.argv[2]
outputFolder = process.argv[3]
indexName = process.argv[4]
typeName = process.argv[5]
bulkSize = process.argv[6]


fs.readdir inputFolder, (err,files) ->
  #console.log "processing #{files}"
  for aFile in files when !fs.lstatSync("#{inputFolder}/#{aFile}").isDirectory() and aFile.substring(0,1) isnt "."
    console.log "converting #{aFile} #{}..." 
    bulkCount = 0
    fs.readFile "#{inputFolder}/#{aFile}", 'utf8',  (err, data) ->
      throw err if err
      obj = JSON.parse(data)
      console.log "... size #{obj.length}" 
      indexLine = "{ \"index\" : { \"_index\" : \"#{indexName}\", \"_type\" : \"#{typeName}\" } }"
      result = ""
      lineCount = 0
      rimraf outputFolder, (err) ->
        mkdirp outputFolder,(err) ->
          throw err if err
          for anItem in obj
            lineCount++
            # switch to bext file when reached bulksize
            if lineCount >= bulkSize
              bulkCount++
              console.log "creating bulk_#{bulkCount}.txt #{lineCount} lines"
              lineCount=0
              
            outputFileName = "file_#{bulkCount}.txt"
            result = "#{indexLine}\n#{JSON.stringify(anItem)}\n"
            fs.appendFile  "#{outputFolder}/#{outputFileName}", result,  (err) ->
              throw err if err

