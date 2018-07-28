local M = Class()

function M:init()
    self.timerList = {}
end



function M:hasTimer(timer)
    local tl = self.timerList

    if not tl or #tl == 0 then
        return false
    end

    if not timer then
        return false
    end

    for _, v in ipairs(tl) do
        if v == timer then
            return true
        end
    end

    return false
end



function M:addTimer(timer)
    if self:hasTimer(timer) then
        return false
    end

    table.insert(self.timerList, timer)
    return true
end



function M:removeTimer(timer)
    local tList = self.timerList

    if not tList or #tList == 0 then
        return false
    end

    for i, v in ipairs(tList) do
        if v == timer then
            v:pause()
            table.remove(tList, i)
            return true
        end
    end

    return false
end



function M:schedule(dt)
    for _, v in ipairs(self.timerList) do
        if v.running then
            v.__time = v.__time + dt

            if v.__time >= v.delay then
                v.__count = v.__count + 1
                v.listener(v, { time = v.__time, count = v.__count })

                v.__time = 0
                if v.iteration ~= 0 and v.__count >= v.iteration then
                    self:removeTimer(v)
                end
            end
        end
    end
end

return M