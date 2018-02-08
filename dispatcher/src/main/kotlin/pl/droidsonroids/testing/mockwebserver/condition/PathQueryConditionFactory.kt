package pl.droidsonroids.testing.mockwebserver.condition

/**
 * Factory which can be used to create similar [PathQueryCondition]s sharing the same path prefix
 * so it does not need to be repeated each time when new [PathQueryCondition] is needed.
 * @param pathPrefix common path prefix
 * @constructor creates new factory with given prefix
 */
class PathQueryConditionFactory(private val pathPrefix: String) {
    /**
     * Creates condition with both <code>path</code>, <code>queryParameterName</code>
     * and <code>queryParameterValue</code>.
     * @param pathSuffix path suffix, may be empty
     * @param queryParameterName query parameter name <code>queryParameterName</code>
     * @param queryParameterValue query parameter value for given
     * @return a PathQueryCondition
     */
    fun withPathSuffixAndQueryParameter(pathSuffix: String, queryParameterName: String, queryParameterValue: String) =
            PathQueryCondition(pathPrefix + pathSuffix, queryParameterName, queryParameterValue)

    /**
     * Creates condition with both <code>path</code>, <code>queryParameterName</code>.
     * @param pathSuffix path suffix, may be empty
     * @param queryParameterName query parameter name <code>queryParameterName</code>
     * @return a PathQueryCondition
     */
    fun withPathSuffixAndQueryParameter(pathSuffix: String, queryParameterName: String) =
            PathQueryCondition(pathPrefix + pathSuffix, queryParameterName)

    /**
     * Creates condition with <code>path</code> only.
     * @param pathSuffix path suffix, may be empty
     * @return a PathQueryCondition
     */
    fun withPathSuffix(pathSuffix: String) =
            PathQueryCondition(pathPrefix + pathSuffix)
}