/*
 * Erp System - Mark X No 11 (Jehoiada Series) Client 1.7.8
 * Copyright © 2021 - 2024 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
// require('dotenv').config();

const webpack = require('webpack');
const { merge } = require('webpack-merge');
const path = require('path');
const BrowserSyncPlugin = require('browser-sync-webpack-plugin');
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const WebpackNotifierPlugin = require('webpack-notifier');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const ESLintPlugin = require('eslint-webpack-plugin');

const environment = require('./environment');
const proxyConfig = require('./proxy.conf');

module.exports = async (config, options, targetOptions) => {
  config.cache = {
    // 1. Set cache type to filesystem
    type: 'filesystem',
    cacheDirectory: path.resolve(__dirname, '../target/webpack'),
    buildDependencies: {
      // 2. Add your config as buildDependency to get cache invalidation on config change
      config: [
        __filename,
        path.resolve(__dirname, 'webpack.custom.js'),
        path.resolve(__dirname, '../angular.json'),
        path.resolve(__dirname, '../tsconfig.app.json'),
        path.resolve(__dirname, '../tsconfig.json'),
      ],
    },
  };

  // PLUGINS
  if (config.mode === 'development') {
    config.plugins.push(
      new ESLintPlugin({
        extensions: ['js', 'ts'],
      }),
      new WebpackNotifierPlugin({
        title: 'Erp System',
        contentImage: path.join(__dirname, 'logo-jhipster.png'),
      })
    );
  }

  // configuring proxy for back end service
  const tls = Boolean(config.devServer && config.devServer.https);
  if (config.devServer) {
    config.devServer.proxy = proxyConfig({ tls });
  }

  if (targetOptions.target === 'serve' || config.watch) {
    config.plugins.push(
      new BrowserSyncPlugin(
        {
          host: 'localhost',
          port: 9000,
          https: tls,
          proxy: {
            target: `http${tls ? 's' : ''}://localhost:${targetOptions.target === 'serve' ? '4200' : '8980'}`,
            ws: true,
            proxyOptions: {
              changeOrigin: false, //pass the Host header to the backend unchanged  https://github.com/Browsersync/browser-sync/issues/430
            },
          },
          socket: {
            clients: {
              heartbeatTimeout: 90000,
            },
          },
          /*
          ghostMode: { // uncomment this part to disable BrowserSync ghostMode; https://github.com/jhipster/generator-jhipster/issues/11116
            clicks: false,
            location: false,
            forms: false,
            scroll: false,
          },
          */
        },
        {
          reload: targetOptions.target === 'build', // enabled for build --watch
        }
      )
    );
  }

  if (config.mode === 'production') {
    config.plugins.push(
      new BundleAnalyzerPlugin({
        analyzerMode: 'static',
        openAnalyzer: false,
        // fallback: {
        //   "child_process": false,
        //   // and also other packages that are not found
        // },
        // Webpack statistics in target folder
        reportFilename: '../target/stats.html',
      })
    );
  }

  const patterns = [
    // jhipster-needle-add-assets-to-webpack - JHipster will add/remove third-party resources in this array
  ];

  if (patterns.length > 0) {
    config.plugins.push(new CopyWebpackPlugin({ patterns }));
  }

  config.plugins.push(
    new webpack.DefinePlugin({
      // APP_VERSION is passed as an environment variable from the Gradle / Maven build tasks.
      __VERSION__: JSON.stringify(environment.__VERSION__),
      __DEBUG_INFO_ENABLED__: environment.__DEBUG_INFO_ENABLED__ || config.mode === 'development',
      // The root URL for API calls, ending with a '/' - for example: `"https://www.jhipster.tech:8081/myservice/"`.
      // If this URL is left empty (""), then it will be relative to the current context.
      // If you use an API server, in `prod` mode, you will need to enable CORS
      // (see the `jhipster.cors` common JHipster property in the `application-*.yml` configurations)
      // SERVER_API_URL: `"http://localhost:8980/"`,
      SERVER_API_URL: process.env.SERVER_API_URL,
    })
  );

  config = merge(
    config
    // jhipster-needle-add-webpack-config - JHipster will add custom config
  );

  return config;
};
