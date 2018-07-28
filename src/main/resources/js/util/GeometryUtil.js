(function () {
    'use strict';

    var ArgCheck = require('./ArgCheck').ArgCheck;

    var GeometryUtil = {};

    GeometryUtil.createRegion = function (left, top, width, height) {
        return {left: Math.ceil(left) || 0, top: Math.ceil(top) || 0, width: Math.ceil(width) || 0, height: Math.ceil(height) || 0};
    };

    GeometryUtil.isRegion = function (object) {
        return object instanceof Object && "left" in object && "top" in object && "width" in object && "height" in object;
    };

    GeometryUtil.createRegionFromLocationAndSize = function (location, size) {
        return GeometryUtil.createRegion(location.x, location.y, size.width, size.height);
    };

    GeometryUtil.isRegionEmpty = function (region) {
        return region.left === 0 && region.top === 0 && region.width === 0 && region.height === 0;
    };

    GeometryUtil.createLocation = function (left, top) {
        return {x: Math.ceil(left) || 0, y: Math.ceil(top) || 0};
    };

    GeometryUtil.isLocation = function (object) {
        return object instanceof Object && "x" in object && "y" in object;
    };

    GeometryUtil.createLocationFromRegion = function (region) {
        return GeometryUtil.createLocation(region.left, region.top);
    };

    GeometryUtil.createLocationFromLocation = function (location) {
        return GeometryUtil.createLocation(location.x, location.y);
    };

    GeometryUtil.createSize = function (width, height) {
        return {width: Math.ceil(width) || 0, height: Math.ceil(height) || 0};
    };

    GeometryUtil.isSize = function (object) {
        return object instanceof Object && "width" in object && "height" in object;
    };

    GeometryUtil.scaleLocation = function (location, scaleRatio) {
        return {x: Math.ceil(location.x * scaleRatio), y: Math.ceil(location.y * scaleRatio)};
    };

    GeometryUtil.scaleSize = function (size, scaleRatio) {
        return {width: Math.ceil(size.width * scaleRatio), height: Math.ceil(size.height * scaleRatio)};
    };

    GeometryUtil.scaleRegion = function (region, scaleRatio) {
        return {width: Math.ceil(region.width * scaleRatio), height: Math.ceil(region.height * scaleRatio), top: Math.ceil(region.top * scaleRatio), left: Math.ceil(region.left * scaleRatio)};
    };

    GeometryUtil.createSizeFromRegion = function (region) {
        return GeometryUtil.createSize(region.width, region.height);
    };

    GeometryUtil.isRegionsIntersected = function (region1, region2) {
        var aRight = region1.left + region1.width, aBottom = region1.top + region1.height, bRight = region2.left + region2.width, bBottom = region2.top + region2.height;
        return (((region1.left <= region2.left && region2.left <= aRight) || (region2.left <= region1.left && region1.left <= bRight)) && ((region1.top <= region2.top && region2.top <= aBottom) || (region2.top <= region1.top && region1.top <= bBottom)));
    };

    GeometryUtil.intersect = function (region1, region2) {
        if (!GeometryUtil.isRegionsIntersected(region1, region2)) return GeometryUtil.createRegion(0, 0, 0, 0);
        var top = Math.max(region1.top, region2.top), left = Math.max(region1.left, region2.left), bottom = Math.min(region1.top + region1.height, region2.top + region2.height), right = Math.min(region1.left + region1.width, region2.left + region2.width);
        return GeometryUtil.createRegion(left, top, right - left, bottom - top);
    };

    GeometryUtil.isRegionContainsLocation = function (region, location) {
        return (region.left <= location.x && (region.left + region.width) > location.x && region.top <= location.y && (region.top + region.height) > location.y);
    };

    GeometryUtil.isRegionContainsRegion = function (region1, region2) {
        var right = this.left + this.width, otherRight = region2.left + region2.width, bottom = this.top + this.height, otherBottom = region2.top + region2.height;
        return this.top <= region2.top && this.left <= region2.left && bottom >= otherBottom && right >= otherRight;
    };

    GeometryUtil.getSubRegions = function (region, subRegionSize, isFixedSize) {
        if (isFixedSize) return GeometryUtil.getSubRegionsWithFixedSize(region, subRegionSize);
        return GeometryUtil.getSubRegionsWithVaryingSize(region, subRegionSize);
    };

    GeometryUtil.getSubRegionsWithFixedSize = function (region, subRegionSize) {
        ArgCheck.notNull(region, "containerRegion");
        ArgCheck.notNull(subRegionSize, "subRegionSize");

        var subRegions = [], subRegionWidth = Math.min(region.width, subRegionSize.width), subRegionHeight = Math.min(region.height, subRegionSize.height);

        if (subRegionWidth === region.width && subRegionHeight === region.height) {
            subRegions.push({left: region.left, top: region.top, width: region.width, height: region.height});
            return subRegions;
        }

        var currentTop = region.top, bottom = region.top + region.height - 1, right = region.left + region.width - 1;

        while (currentTop <= bottom) {
            if (currentTop + subRegionHeight > bottom) currentTop = (bottom - subRegionHeight) + 1;

            var currentLeft = region.left;
            while (currentLeft <= right) {
                if (currentLeft + subRegionWidth > right) currentLeft = (right - subRegionWidth) + 1;
                subRegions.push({left: currentLeft, top: currentTop, width: subRegionWidth, height: subRegionHeight});
                currentLeft += subRegionWidth;
            }
            currentTop += subRegionHeight;
        }
        return subRegions;
    };

    GeometryUtil.getSubRegionsWithVaryingSize = function (region, maxSubRegionSize) {
        ArgCheck.notNull(region, "containerRegion");
        ArgCheck.notNull(maxSubRegionSize, "maxSubRegionSize");
        ArgCheck.greaterThanZero(maxSubRegionSize.width, "maxSubRegionSize.width");
        ArgCheck.greaterThanZero(maxSubRegionSize.height, "maxSubRegionSize.height");

        var subRegions = [], currentTop = region.top, bottom = region.top + region.height,
            right = region.left + region.width;

        while (currentTop < bottom) {

            var currentBottom = currentTop + maxSubRegionSize.height;
            if (currentBottom > bottom) currentBottom = bottom;

            var currentLeft = region.left;
            while (currentLeft < right) {
                var currentRight = currentLeft + maxSubRegionSize.width;
                if (currentRight > right) currentRight = right;

                var currentHeight = currentBottom - currentTop, currentWidth = currentRight - currentLeft;

                subRegions.push({left: currentLeft, top: currentTop, width: currentWidth, height: currentHeight});
                currentLeft += maxSubRegionSize.width;
            }
            currentTop += maxSubRegionSize.height;
        }
        return subRegions;
    };

    GeometryUtil.locationOffset = function (location, offset) {
        return {x: location.x + offset.x, y: location.y + offset.y};
    };

    GeometryUtil.regionOffset = function (region, offset) {
        return {top: region.top + offset.x, left: region.left + offset.y, width: region.width, height: region.height};
    };

    GeometryUtil.getMiddleOffsetOfRegion = function (region) {
        return {x: region.width / 2, y: region.height / 2};
    };

    exports.GeometryUtil = GeometryUtil;
}());