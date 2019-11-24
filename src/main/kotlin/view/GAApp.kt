package by.vkiva.model.view

import tornadofx.App
import view.FunctionView

//(0.7t-1.7)*cos(0.5pi*t+1.5) t[-5,5]
//10000 - parts
//2^14
class GAApp: App(FunctionView::class)