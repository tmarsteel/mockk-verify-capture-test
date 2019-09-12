package com.acme

import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class ExampleTest {
    @Test
    fun test() {
        val mock = mockk<MockedSubject>()

        every { mock.doSomething("1", "data1") } returns "result1"
        every { mock.doSomething("2", "data2") } returns "result2"

        mock.doSomething("1", "data1")
        mock.doSomething("2", "data2")

        val dataSlotId1 = slot<String>()
        val dataSlotId2 = slot<String>()

        verify (exactly = 1) { mock.doSomething("1", capture(dataSlotId1)) }
        verify (exactly = 1) { mock.doSomething("2", capture(dataSlotId2)) }

        dataSlotId1.captured shouldBe "data1"
        dataSlotId2.captured shouldBe "data2"
    }

    @Test
    fun test2() {
        val mock = mockk<MockedSubject>()

        every { mock.doSomething("1", "data1") } returns "result1"
        every { mock.doSomething("2", "data2") } returns "result2"

        mock.doSomething("1", "data1")
        mock.doSomething("2", "data2")

        val dataSlot = slot<String>()
        verify (exactly = 1) { mock.doSomething("1", capture(dataSlot)) }
        dataSlot.captured shouldBe "data1"

        dataSlot.clear()
        verify (exactly = 1) { mock.doSomething("2", capture(dataSlot)) }
        dataSlot.captured shouldBe "data2"
    }

    @Test
    fun mockito() {
        val mock = Mockito.mock(MockedSubject::class.java)

        Mockito.`when`(mock.doSomething("1", "data1")).thenReturn("result1")
        Mockito.`when`(mock.doSomething("2", "data2")).thenReturn("result2")

        mock.doSomething("1", "data1")
        mock.doSomething("2", "data2")

        val dataId1Captor = ArgumentCaptor.forClass(java.lang.String::class.java)
        val dataId2Captor = ArgumentCaptor.forClass(java.lang.String::class.java)

        Mockito.verify(mock, Mockito.times(1)).doSomething(ArgumentMatchers.eq("1"), dataId1Captor.capture())
        Mockito.verify(mock, Mockito.times(1)).doSomething(ArgumentMatchers.eq("2"), dataId2Captor.capture())

        dataId1Captor.value shouldBe "data1"
        dataId2Captor.value shouldBe "data2"
    }

    open class MockedSubject {
        open fun doSomething(id: String?, data: Any?): String {
            throw IllegalStateException("Not mocked :(")
        }
    }
}
