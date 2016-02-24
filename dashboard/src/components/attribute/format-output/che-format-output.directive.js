/*
 * Copyright (c) 2015-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 */
'use strict';

/**
 * Defines a directive for formatting output.
 * @author Ann Shumilova
 */
export class CheFormatOutput {

  /**
   * Default constructor that is using resource
   * @ngInject for Dependency injection
   */
  constructor(jsonOutputColors, $compile) {
    this.restrict = 'A';
    this.outputColors = angular.fromJson(jsonOutputColors);
    this.$compile = $compile;
  }

  /**
   * Keep reference to the model controller
   */
  link($scope, element, attr) {
    $scope.$watch(attr.ngModel, (value) => {
      if (!value || value.length === 0) {
        return;
      }

      let lines = value.split('\n');
      let result = '';

      lines.forEach((line) => {

        for (var i = 0; i < this.outputColors.length; i++ ) {
          let outputColor = this.outputColors[i];
          if (line.startsWith('[' + outputColor.type + ']')) {
            line = line.replace('[' + outputColor.type + ']', '<span style=\"color: ' + outputColor.color + '\">[' + outputColor.type + ']</span>');
            line = '<span>' + line + '</span>';
            break;
          }
        }

        result += '<br/>' + line;
      });
      result = '<span>' + result + '</span>';
      element.html(this.$compile(result)($scope));
    });
  }
}
