package org.jetbrains.ktor.pipeline

internal class PipelineExecution(
        val machine: PipelineMachine,
        override val subject: Any,
        functions: List<PipelineContext<Any>.(Any) -> Unit>) : PipelineContext<Any> {

    override fun onSuccess(body: PipelineContext<Any>.(Any) -> Unit) {
        blocks[blockIndex - 1].successes.add(body)
    }

    override fun onFail(body: PipelineContext<Any>.(Any) -> Unit) {
        blocks[blockIndex - 1].failures.add(body)
    }

    override fun <T : Any> fork(value: T, pipeline: Pipeline<T>) = machine.execute(value, pipeline)
    override fun pause(): Nothing = machine.pause()
    override fun proceed() = machine.proceed()
    override fun fail(exception: Throwable): Nothing = machine.fail(exception)
    override fun finish(): Nothing = machine.finish()
    override fun finishAll(): Nothing = machine.finishAll()

    val blocks = functions.map { PipelineBlock(it) }
    var blockIndex = 0

    var state = PipelineState.Executing
    override var exception: Throwable? = null
}

