import { join } from "https://deno.land/std@0.183.0/path/mod.ts"
import { brightRed } from "https://deno.land/std@0.183.0/fmt/colors.ts"
import { resolve } from "https://deno.land/std@0.183.0/path/mod.ts"
import { magenta } from "https://deno.land/std@0.183.0/fmt/colors.ts"

import promiseObject from "https://deno.land/x/promise_object@v0.10.0/index.ts"

import { match, P } from "npm:ts-pattern"

import { hardlink, inode } from "./inode.ts"
import { fmt, Logger } from "./logger.ts"
import { noop } from "./noop.ts"
import { parse } from "https://deno.land/std@0.183.0/path/win32.ts"

export type LinkMeta = {
  from: string
  target: string
  fromIno: number
  targetIno: number
  isHardLinked: boolean
}

const hardLinkMeta =
  (from: string) => async (to: string): Promise<LinkMeta> => {
    const target = join(to, parse(from).base)
    const { fromIno, targetIno } = await promiseObject({
      fromIno: inode(from)(),
      targetIno: inode(target)(),
    })
    const isHardLinked = fromIno === targetIno

    return { from, target, fromIno, targetIno, isHardLinked }
  }

// deno-fmt-ignore
export type LinkAction =
  (l: Logger) => (f: string) => (t: string) => Promise<void>

export const hardlinkError: LinkAction = (log) => () => () => {
  log(brightRed("paths are not hard linked."))
  Deno.exit(1)
}

export const createHardLink: LinkAction = (log) => (from) => async (target) => {
  await hardlink(from)(target)
  log(magenta(`linked ${resolve(from)} -> ${target}`))
}

// deno-fmt-ignore
type ForceCreateLinkTo =
  (l: Logger) => (a: LinkAction) => (f: string) => (t: string) => Promise<void>
export const forceCreateLinkTo: ForceCreateLinkTo =
  (log) => (action) => (from) => async (to) => {
    const meta = await hardLinkMeta(from)(to)
    const doHardLink = (target: string) => action(log)(from)(target)

    fmt(log)(meta)

    await match(meta)
      .with({ isHardLinked: false, target: P.select() }, doHardLink)
      .otherwise(noop)
  }
