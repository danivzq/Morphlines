@Library('libpipelines@feature/multibranch') _

hose {
    EMAIL = 'morphlines'
    MODULE = 'morphlines'
    REPOSITORY = 'morphlines'
    DEVTIMEOUT = 35
    RELEASETIMEOUT = 35
    FOSS = true

    ITSERVICES = [
        ['MONGODB': [
           'image': 'stratio/mongo:3.0.4',
           'sleep': 30,
           'healthcheck': 27017]],
    ]
    
    ITPARAMETERS = """
        | -Dmongo.ip=%%MONGODB
        | -Dmongo.port=27017"""
    
    DEV = { config ->        
        doCompile(config)
        
        parallel(UT: {
            doUT(config)
        }, IT: {
            doIT(config)
        }, failFast: config.FAILFAST)

        doPackage(config)
        
        parallel(QC: {
            doStaticAnalysis(config)
        }, DOC: {
            doDoc(config)
        }, DEPLOY: {
            doDeploy(config)
        }, failFast: config.FAILFAST)
     }    
}
