local M = Class()

function M:init(delay, iteration, listener)
    self.delay = delay or 1
    self.iteration = iteration or 1
    self.listener = listener
    self.running = true

    self.__time = 0
    self.__count = 0
end

function M:isRunning()
    return self.running
end

function M:resume()
    self.running = true
end

function M:pause()
    self.running = false
end

return M