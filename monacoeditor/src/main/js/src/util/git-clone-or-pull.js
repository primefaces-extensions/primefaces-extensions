// @ts-check

// Adapted from the below. Modified to add better error output, additional
// options, and support for promises.
/*! git-pull-or-clone. MIT License. Feross Aboukhadijeh <https://feross.org/opensource> */

import crossSpawn from "cross-spawn";
import { constants as fsConst, promises as fs } from "node:fs";

/**
 * @typedef {{
 * depth?: number;
 * logGitOutput?: boolean;
 * reset?: boolean;
 * clean?: boolean;
 * force?: boolean;
 * rebase?: boolean;
 * strategy?: string;
 * strategyOptions?: string[];
 * allowUnrelatedHistories?: boolean;
 * }} GitPullOrCloneOpts
 */
undefined;

/**
 * @typedef {{
 * exitCode: number;
 * output: () => string;
 * }} SpawnResult
 */
undefined;

/**
 * @param {SpawnResult} spawnResult
 * @param {GitPullOrCloneOpts} opts
 */
function handleSpawnResult(spawnResult, opts) {
  if (spawnResult.exitCode !== 0) {
    throw new Error(`Non-zero exit code: ${spawnResult.exitCode}\n${spawnResult.output()}`);
  }

  if (opts.logGitOutput) {
    console.log(spawnResult.output());
  }
}

/**
 * @param {string} command 
 * @param {string[]} args 
 * @param {import("child_process").SpawnOptions} opts
 * @returns {Promise<SpawnResult>}
 */
function spawnAndWaitUntilTerminated(command, args, opts) {
  console.log(`> ${command} ${args.join(" ")}`);
  return new Promise((resolve, reject) => {
    /** @type {string[]} */
    const out = [];
    const child = crossSpawn(command, args, {
      stdio: "pipe",
      ...opts,
    });
    child.stdout?.on("data", chunk => {
      out.push(chunk.toString("utf8"));
    });
    child.stderr?.on("data", chunk => {
      out.push(chunk.toString("utf8"));
    });
    child.on("error", error => reject(error));
    child.on("close", code => {
      resolve({
        exitCode: code ?? 0,
        output: () => out.join(""),
      });
    });
  });
}

/**
 * @param {string} command
 * @param {GitPullOrCloneOpts} opts
 * @returns {string[]}
 */
function createGitArgs(command, opts) {
  const args = [];

  if (command === "clone" || command === "pull") {
    // --depth implies --single-branch
    const depth = opts.depth ?? 1;
    if (depth < Infinity) {
      args.push(`--depth=${depth}`);
    }
    else {
      args.push("--single-branch");
    }
  }

  if (command === "pull") {
    if (opts.force === true) {
      args.push("--force");
    }

    if (opts.allowUnrelatedHistories === true) {
      args.push("--allow-unrelated-histories");
    }

    if (opts.strategy !== undefined) {
      args.push("-s");
      args.push(opts.strategy);
    }
    for (const strategyOption of opts.strategyOptions ?? []) {
      args.push("-X");
      args.push(strategyOption);
    }

    if (opts.rebase === true) {
      args.push("--rebase");
    }
    else {
      args.push("--no-rebase");
    }
  }

  return args;
}

/**
 * @param {string} url
 * @param {string} outPath
 * @param {GitPullOrCloneOpts} opts 
 * @returns {Promise<void>}
 */
async function gitClone(url, outPath, opts) {
  const args = [
    "clone",
    ...createGitArgs("clone", opts),
    url,
    outPath,
  ];

  const spawnResult = await spawnAndWaitUntilTerminated("git", args, {
  });

  handleSpawnResult(spawnResult, opts);
}

/**
 * @param {string} outPath
 * @param {GitPullOrCloneOpts} opts 
 * @returns {Promise<void>}
 */
async function gitClean(outPath, opts) {
  const args = [
    "clean",
    "-xfd",
  ];

  const spawnResult = await spawnAndWaitUntilTerminated("git", args, {
    cwd: outPath,
  });

  handleSpawnResult(spawnResult, opts);
}

/**
 * @param {string} outPath
 * @param {GitPullOrCloneOpts} opts 
 * @returns {Promise<void>}
 */
async function gitResetHard(outPath, opts) {
  const args = [
    "reset",
    "--hard",
  ];

  const spawnResult = await spawnAndWaitUntilTerminated("git", args, {
    cwd: outPath,
  });

  handleSpawnResult(spawnResult, opts);
}

/**
 * @param {string} outPath
 * @param {GitPullOrCloneOpts} opts 
 * @returns {Promise<void>}
 */
async function gitPull(outPath, opts) {
  const args = [
    "pull",
    ...createGitArgs("pull", opts),
  ];

  const spawnResult = await spawnAndWaitUntilTerminated("git", args, {
    cwd: outPath,
  });

  handleSpawnResult(spawnResult, opts);
}

/**
 * @param {string} path 
 * @returns {Promise<boolean>} 
 */
async function isGitDirectory(path) {
  try {
    await fs.access(path, fsConst.R_OK | fsConst.W_OK);
    return true;
  }
  catch (err) {
    return false;
  }
}

/**
 * @param {string} url 
 * @param {string} outPath 
 * @param {GitPullOrCloneOpts} [opts] 
 * @returns {Promise<void>}
 */
export async function gitPullOrClone(url, outPath, opts = {}) {
  if (await isGitDirectory(outPath)) {
    if (opts.reset === true) {
      await gitResetHard(outPath, opts);
    }
    if (opts.clean === true) {
      await gitClean(outPath, opts);
    }
    await gitPull(outPath, opts);
  }
  else {
    await gitClone(url, outPath, opts);
  }
}

/**
 * Clones the URL to the given out path if the out path does not exist. Otherwise,
 * pulls from the origin, discarding all local changes.
 * @param {string} url 
 * @param {string} outPath 
 * @returns {Promise<void>}
 */
export async function gitUpdateToTheirs(url, outPath) {
  return gitPullOrClone(url, outPath, {
    allowUnrelatedHistories: true,
    clean: true,
    force: true,
    rebase: false,
    reset: true,
    strategyOptions: [
      "theirs",
    ],
  });
}